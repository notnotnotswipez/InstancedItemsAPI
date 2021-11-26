package me.swipez.instanceditemsapi.demo;

import me.swipez.instanceditemsapi.InstancedItem;
import me.swipez.instanceditemsapi.annotations.DeclaredModule;
import me.swipez.instanceditemsapi.stockModules.RightClickItemModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TestItem extends InstancedItem {
    public TestItem() {
        super("testitem", new ItemStack(Material.DIRT));
    }

    int timesRun = 0;

    @DeclaredModule(moduleClass = RightClickItemModule.class)
    public void onPlayerTestRightClicks(RightClickItemModule.Data data){
        data.getPlayer().sendMessage(ChatColor.RED+"This worked.");
        timesRun++;
        Bukkit.broadcastMessage(ChatColor.RED+""+timesRun+"");
        data.getEvent().setCancelled(true);
    }

}
