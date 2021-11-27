package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KilledEntityItemModule extends ItemModule {

    public KilledEntityItemModule(){

    }

    @EventHandler
    public void onEntityDies(EntityDeathEvent event){
        if (event.getEntity().getKiller() == null){
            return;
        }
        Player player = event.getEntity().getKiller();
        if (isItem(player.getInventory().getItemInMainHand())){
            trigger(new Data(player, event.getEntity(), event));
        }
    }

    public static class Data {

        final Player player;
        final Entity killed;
        final EntityDeathEvent event;


        public Data(Player player, Entity killed, EntityDeathEvent event) {
            this.player = player;
            this.killed = killed;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public EntityDeathEvent getEvent() {
            return event;
        }

        public Entity getKilled() {
            return killed;
        }
    }
}
