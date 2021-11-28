package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BrokenBlockItemModule extends ItemModule {


    public BrokenBlockItemModule(){

    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event){
        if (isItem(event.getPlayer().getInventory().getItemInMainHand())){
            trigger(new Data(event.getPlayer(), event));
        }
    }

    public static class Data {

        final Player player;
        final BlockBreakEvent event;


        public Data(Player player, BlockBreakEvent event) {
            this.player = player;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public BlockBreakEvent getEvent() {
            return event;
        }
    }
}
