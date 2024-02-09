package gg.flyte.twilight.extension

import gg.flyte.twilight.string.translate
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.net.MalformedURLException
import java.net.URL
import java.util.*

fun ItemStack.name(name: String) {
    val meta = itemMeta
    meta?.setDisplayName(name.translate())
    itemMeta = meta
}

fun ItemStack.lore(vararg lore: String) {
    val meta = itemMeta
    meta?.lore = lore.toList().translate()
    itemMeta = meta
}

fun ItemStack.clearLore() {
    val meta = itemMeta
    meta?.lore = null
    itemMeta = meta
}

fun ItemStack.enchant(enchantment: Enchantment, level: Int = 1) {
    addUnsafeEnchantment(enchantment, level)
}

fun ItemStack.unenchant(enchantment: Enchantment) {
    val meta = itemMeta
    meta?.removeEnchant(enchantment)
    itemMeta = meta
}

fun ItemStack.unenchant() {
    itemMeta?.enchants?.keys?.forEach { unenchant(it) }
}

fun ItemStack.flags(vararg flags: ItemFlag) {
    val meta = itemMeta
    meta?.addItemFlags(*flags)
    itemMeta = meta
}

fun ItemStack.glow() {
    enchant(Enchantment.WATER_WORKER)
    flags(ItemFlag.HIDE_ENCHANTS)
}

fun ItemStack.potion(type: PotionType, extended: Boolean = false, upgraded: Boolean = false) {
    val meta = itemMeta as PotionMeta
    meta.basePotionData = PotionData(type, extended, upgraded)
    itemMeta = meta
}

fun ItemStack.texture(texture: String) {
    val profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID())
    val textures = profile.textures
    try {
        textures.skin = URL("https://textures.minecraft.net/texture/$texture")
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    profile.setTextures(textures)
    val meta = itemMeta as SkullMeta
    meta.ownerProfile = profile
    itemMeta = meta
}

fun ItemStack.model(modelData: Int) {
    type = Material.PAPER
    val meta = itemMeta
    meta?.setCustomModelData(modelData)
    itemMeta = meta
}