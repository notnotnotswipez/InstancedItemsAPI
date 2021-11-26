package me.swipez.instanceditemsapi;

import me.swipez.instanceditemsapi.test.TestItem;
import me.swipez.instanceditemsapi.test.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class InstancedItemsAPI extends JavaPlugin {

    private static InstancedItemsAPI plugin;
    public static boolean debug = false;

    @Override
    public void onEnable() {
        plugin = this;
        if (debug){
            InstancedItemsRegistry.register(TestItem.class, "testitem");
            getCommand("testcommand").setExecutor(new TestCommand());
        }
    }

    @Override
    public void onDisable() {

    }

    public static InstancedItemsAPI getPlugin() {
        return plugin;
    }
}
