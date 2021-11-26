package me.swipez.instanceditemsapi;

import org.bukkit.plugin.java.JavaPlugin;

public final class InstancedItemsAPI {

    private static JavaPlugin plugin;
    public static boolean debug = false;

    public static void setPlugin(JavaPlugin javaPlugin){
        plugin = javaPlugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
