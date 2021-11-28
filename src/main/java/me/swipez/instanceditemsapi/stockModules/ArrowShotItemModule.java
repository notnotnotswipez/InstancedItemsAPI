package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ArrowShotItemModule extends ItemModule {

    public ArrowShotItemModule(){

    }

    @EventHandler
    public void onEntityShootsArrow(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            if (isItem(event.getBow())){
                trigger(new Data((Player) event.getEntity(), event));
            }
        }
    }

    public static class Data {

        final Player player;
        final EntityShootBowEvent event;


        public Data(Player player, EntityShootBowEvent event) {
            this.player = player;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public EntityShootBowEvent getEvent() {
            return event;
        }
    }
}
