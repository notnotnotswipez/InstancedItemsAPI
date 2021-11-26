package me.swipez.instanceditemsapi.demo;

import me.swipez.instanceditemsapi.InstancedItem;
import me.swipez.instanceditemsapi.InstancedItemsRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            InstancedItem newItem = InstancedItemsRegistry.getCachedInstance("testitem").copy(TestItem.class);
            player.getInventory().addItem(newItem.getBaseItem());
        }
        return true;
    }
}
