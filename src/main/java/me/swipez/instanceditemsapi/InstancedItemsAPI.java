package me.swipez.instanceditemsapi;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class InstancedItemsAPI {

    private static JavaPlugin plugin;

    public static void setPlugin(JavaPlugin javaPlugin){
        plugin = javaPlugin;
        System.out.println(ChatColor.GREEN+"Instanced Items API "+ChatColor.BLUE+"initialized for "+plugin.getName());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
