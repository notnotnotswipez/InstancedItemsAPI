package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitEntityItemModule extends ItemModule {

    public HitEntityItemModule(){

    }

    @EventHandler
    public void onPlayerHitsMob(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) event.getDamager();
        if (isItem(player.getInventory().getItemInMainHand())){
            trigger(new Data(player, event.getEntity(), event));
        }
    }


    public static class Data {

        final Player player;
        final Entity damaged;
        final EntityDamageByEntityEvent event;


        public Data(Player player, Entity damaged, EntityDamageByEntityEvent event) {
            this.player = player;
            this.damaged = damaged;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public EntityDamageByEntityEvent getEvent() {
            return event;
        }

        public Entity getDamaged() {
            return damaged;
        }
    }
}
