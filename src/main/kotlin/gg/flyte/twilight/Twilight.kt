package gg.flyte.twilight

import com.google.gson.GsonBuilder
import gg.flyte.twilight.builders.item.ItemBuilder
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.service.NameCacheService
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.event.custom.chat.command.ChatClickCommand
import gg.flyte.twilight.event.customEventListeners
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.data.Redis
import gg.flyte.twilight.gson.adapter.ConfigurationSerializationTypeAdapter
import gg.flyte.twilight.gson.adapter.LocalDateTimeTypeAdapter
import gg.flyte.twilight.server.ServerSoftware
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.banner.Pattern
import org.bukkit.block.spawner.SpawnRule
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.profile.PlayerProfile
import org.bukkit.util.BlockVector
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.time.LocalDateTime

class Twilight(javaPlugin: JavaPlugin) {

    private var gsonBuilder: GsonBuilder = GsonBuilder()

    init {
        plugin = javaPlugin
        Companion.gsonBuilder = gsonBuilder
        run {
            ServerSoftware
            customEventListeners
            ItemBuilder.Companion
            ChatClickCommand.register()
        }
    }

    companion object {
        lateinit var plugin: JavaPlugin
        var gsonBuilder: GsonBuilder = GsonBuilder().setPrettyPrinting()
        var usingEnv = false
        val internalPdc by lazy { "_twilight_${plugin.name.lowercase()}" }

        fun isFolia(): Boolean = ServerSoftware.isFolia()
        fun isPaper(): Boolean = ServerSoftware.isPaper()
        fun isSpigot(): Boolean = ServerSoftware.isSpigot()
        fun isCraftBukkit(): Boolean = ServerSoftware.isCraftBukkit()
    }

    fun env(init: Environment.Settings.() -> Unit = {}) {
        usingEnv = true
        Environment.env(Environment.Settings().apply(init))
    }

    fun mongo(init: MongoDB.Settings.() -> Unit = {}) = MongoDB.mongo(MongoDB.Settings().apply(init))

    fun redis(init: Redis.Settings.() -> Unit = {}) = Redis.redis(Redis.Settings().apply(init))

    fun nameCache(init: NameCacheService.Settings.() -> Unit = {}) = NameCacheService.nameCache(NameCacheService.Settings().apply(init))

    fun gson(registerDefaults: Boolean = true, init: GsonBuilder.() -> Unit) {
        gsonBuilder = gsonBuilder.apply {
            if (registerDefaults) {
                registerTypeAdapter(AttributeModifier::class.java, ConfigurationSerializationTypeAdapter<AttributeModifier>())
                registerTypeAdapter(BlockVector::class.java, ConfigurationSerializationTypeAdapter<BlockVector>())
                registerTypeAdapter(BoundingBox::class.java, ConfigurationSerializationTypeAdapter<BoundingBox>())
                registerTypeAdapter(Color::class.java, ConfigurationSerializationTypeAdapter<Color>())
                registerTypeAdapter(FireworkEffect::class.java, ConfigurationSerializationTypeAdapter<FireworkEffect>())
                registerTypeAdapter(ItemStack::class.java, ConfigurationSerializationTypeAdapter<ItemStack>())
                registerTypeAdapter(Location::class.java, ConfigurationSerializationTypeAdapter<Location>())
                registerTypeAdapter(Pattern::class.java, ConfigurationSerializationTypeAdapter<Pattern>())
                registerTypeAdapter(PotionEffect::class.java, ConfigurationSerializationTypeAdapter<PotionEffect>())
                registerTypeAdapter(SpawnRule::class.java, ConfigurationSerializationTypeAdapter<SpawnRule>())
                registerTypeAdapter(Vector::class.java, ConfigurationSerializationTypeAdapter<Vector>())

                registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            }

            init()
        }
    }

    fun terminate() = customEventListeners.applyForEach { unregister() }
}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit = {}): Twilight = Twilight(plugin).apply(init)
