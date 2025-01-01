package gg.flyte.twilight.server

enum class ServerSoftware {

    FOLIA,
    PAPER,
    SPIGOT,
    CRAFT_BUKKIT;

    companion object {
        val current: ServerSoftware by lazy {
            val hasClass = { name: String -> try { Class.forName(name); true } catch (e: ClassNotFoundException) { false } }

            if (hasClass("io.papermc.paper.threadedregions.RegionizedServer")) {
                FOLIA
            } else if (hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration")) {
                PAPER
            } else if (hasClass("org.spigotmc.SpigotConfig")) {
                SPIGOT
            } else {
                CRAFT_BUKKIT
            }
        }

        fun isFolia(): Boolean = current == FOLIA
        fun isPaper(): Boolean = current == PAPER
        fun isSpigot(): Boolean = current == SPIGOT
        fun isCraftBukkit(): Boolean = current == CRAFT_BUKKIT
    }

}