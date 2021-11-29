package me.swipez.instanceditemsapi;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class InstancedItemsAPI {

    private static JavaPlugin plugin;

    public static void setPlugin(JavaPlugin javaPlugin){
        plugin = javaPlugin;
        plugin.getLogger().log(Level.INFO,ChatColor.GREEN+"[Instanced Items API] "+ChatColor.BLUE+"Initialized for "+plugin.getName());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
