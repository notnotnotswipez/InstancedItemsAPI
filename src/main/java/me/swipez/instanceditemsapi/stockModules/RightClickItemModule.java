package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class RightClickItemModule extends ItemModule {

    public RightClickItemModule(){

    }

    @EventHandler
    public void onPlayerRightClicksWithItem(PlayerInteractEvent event) {
        if (!event.getAction().toString().toLowerCase().contains("right")){
            return;
        }
        if (isItem(event.getItem())){
            trigger(new Data(event.getPlayer(), event));
        }
    }


    public static class Data {

        final Player player;
        final PlayerInteractEvent event;


        public Data(Player player, PlayerInteractEvent event) {
            this.player = player;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public PlayerInteractEvent getEvent() {
            return event;
        }
    }
}
