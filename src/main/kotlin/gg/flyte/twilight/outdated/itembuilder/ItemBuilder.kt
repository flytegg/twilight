package gg.flyte.twilight.outdated.itembuilder

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.event
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ItemBuilder(
    var type: Material,
    val amount: Int = 1,
    var name: Component? = null,
    var lore: MutableList<Component>? = null,
    var unbreakable: Boolean = false,
    val enchantments: MutableMap<Enchantment, Int> = HashMap(),
    var customModelData: Int? = null,
    var attributes: MutableMap<Attribute, AttributeModifier> = HashMap(),

    val persistentStrings: MutableMap<String, String> = HashMap<String, String>().apply {
        put(INTERACTION_UUID_KEY, uuidPdc())
    },

    val persistentInts: MutableMap<String, Int> = HashMap(),
    val persistentDoubles: MutableMap<String, Double> = HashMap(),
    val persistentFloats: MutableMap<String, Float> = HashMap(),
    val persistentLongs: MutableMap<String, Long> = HashMap(),
    val persistentBooleans: MutableMap<String, Boolean> = HashMap(),

    block: ItemBuilder.() -> Unit = {}
) {

    init { apply(block) }

    companion object {
        const val INTERACTION_UUID_KEY = "interaction_uuid"

        private val clickInteractions =
            mutableMapOf<String, HashMap<ItemInteraction, HashSet<PlayerInteractEvent.() -> Unit>>>()

        private val dropInteractions =
            mutableMapOf<String, HashSet<PlayerDropItemEvent.() -> Unit>>()

        init {
            fun invoke(item: ItemStack, interaction: ItemInteraction, event: Event) {
                when (interaction) {
                    ItemInteraction.RIGHT,
                    ItemInteraction.LEFT -> {
                        clickInteractions[item.getTwilightInteractUuid()]
                            ?.get(interaction)
                            ?.forEach { it.invoke(event as PlayerInteractEvent) }
                    }

                    ItemInteraction.DROP -> {
                        dropInteractions[item.getTwilightInteractUuid()]
                            ?.forEach { it.invoke(event as PlayerDropItemEvent) }
                    }
                }
            }

            event<PlayerInteractEvent> {
                item?.let { item ->
                    when (action) {
                        Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                            invoke(item, ItemInteraction.RIGHT, this)
                        }

                        Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                            invoke(item, ItemInteraction.LEFT, this)
                        }

                        else -> {}
                    }
                }
            }

            event<PlayerDropItemEvent> { invoke(itemDrop.itemStack, ItemInteraction.DROP, this) }

        }

        fun ItemStack.getTwilightInteractUuid() = itemMeta
            ?.persistentDataContainer
            ?.get(
                NamespacedKey(
                    Twilight.internalPdc,
                    INTERACTION_UUID_KEY
                ),
                PersistentDataType.STRING
            )

    }

    fun build(): ItemStack {
        ItemStack(type, amount).apply {
            itemMeta = itemMeta?.apply {
                this.isUnbreakable = this@ItemBuilder.unbreakable

                this.lore(this@ItemBuilder.lore)
                this.displayName(this@ItemBuilder.name)
                this.setCustomModelData(this@ItemBuilder.customModelData)

                this@ItemBuilder.enchantments
                    .forEach { (enchantment, level) -> addUnsafeEnchantment(enchantment, level) }

                this@ItemBuilder.attributes
                    .forEach { (attribute, modifier) -> addAttributeModifier(attribute, modifier) }

                persistentDataContainer.apply {
                    fun getKey(key: String) = NamespacedKey(Twilight.internalPdc, key)

                    persistentStrings.forEach { (key, value) -> set(getKey(key), PersistentDataType.STRING, value) }
                    persistentInts.forEach { (key, value) -> set(getKey(key), PersistentDataType.INTEGER, value) }
                    persistentDoubles.forEach { (key, value) -> set(getKey(key), PersistentDataType.DOUBLE, value) }
                    persistentFloats.forEach { (key, value) -> set(getKey(key), PersistentDataType.FLOAT, value) }
                    persistentLongs.forEach { (key, value) -> set(getKey(key), PersistentDataType.LONG, value) }
                    persistentBooleans.forEach { (key, value) -> set(getKey(key), PersistentDataType.BOOLEAN, value) }
                }
            }
        }.also { item -> return item }
    }

    private fun getInteractionKey(): String = persistentStrings[INTERACTION_UUID_KEY] as String

    fun onRightClick(block: PlayerInteractEvent.() -> Unit) {
        clickInteractions
            .getOrPut(getInteractionKey()) { HashMap() }
            .getOrPut(ItemInteraction.RIGHT) { HashSet() }
            .add(block)
    }

    fun onLeftClick(block: PlayerInteractEvent.() -> Unit) {
        clickInteractions
            .getOrPut(getInteractionKey()) { HashMap() }
            .getOrPut(ItemInteraction.LEFT) { HashSet() }
            .add(block)
    }

    fun onDrop(block: PlayerDropItemEvent.() -> Unit) {
        dropInteractions
            .getOrPut(getInteractionKey()) { HashSet() }
            .add(block)
    }
}
