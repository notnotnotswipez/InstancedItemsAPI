package me.swipez.instanceditemsapi.stockModules;

import me.swipez.instanceditemsapi.InstancedItemsAPI;
import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArrowHitItemModule extends ItemModule {

    public ArrowHitItemModule(){
        new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> forgottenArrows = new ArrayList<>();
                for (UUID uuid : shotArrows){
                    Projectile projectile = (Projectile) Bukkit.getEntity(uuid);
                    if (projectile.isInWater() || projectile.isOnGround()){
                        trigger(new Data((Player) projectile.getShooter(), null, null, false, projectile));
                        forgottenArrows.add(uuid);
                    }
                }
                for (UUID uuid : forgottenArrows){
                    shotArrows.remove(uuid);
                }
                if (!shotArrows.isEmpty()){
                    shotArrows.removeIf(uuid -> (Bukkit.getEntity(uuid) == null));
                    shotArrows.removeIf(uuid -> !(Bukkit.getEntity(uuid).isValid()));
                }
            }
        }.runTaskTimer(InstancedItemsAPI.getPlugin(), 1, 1);
    }

    List<UUID> shotArrows = new ArrayList<>();

    @EventHandler
    public void onPlayerShootsBow(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            if (isItem(event.getBow())){
                shotArrows.add(event.getProjectile().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onArrowHits(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) event.getDamager();
            if (shotArrows.contains(projectile.getUniqueId())) {
                Player player = (Player) projectile.getShooter();
                trigger(new Data(player, event.getEntity(), event, true, projectile));
            }
        }
    }

    public static class Data {

        final Player player;
        final Entity damaged;
        final EntityDamageByEntityEvent event;
        final boolean hitEntity;
        final Projectile projectile;

        public Data(Player player, Entity damaged, EntityDamageByEntityEvent event, boolean hitEntity, Projectile projectile) {
            this.player = player;
            this.damaged = damaged;
            this.event = event;
            this.hitEntity = hitEntity;
            this.projectile = projectile;
        }

        public Projectile getProjectile() {
            return projectile;
        }

        public boolean hasHitEntity() {
            return hitEntity;
        }

        public Player getPlayer() {
            return player;
        }

        /**
         * Will return the event only if the arrow hit a mob. Otherwise,
         * will be null.
         *
         * Should be checked by hasHitEntity()
         *
         * @return An EntityDamageEvent
         */
        public EntityDamageByEntityEvent getEvent() {
            return event;
        }

        /**
         * Will return an entity that was damaged only if the arrow has hit a mob. Otherwise,
         * will be null.
         *
         * Should be checked by hasHitEntity()
         *
         * @return The entity that was shot by the arrow
         */
        public Entity getDamaged() {
            return damaged;
        }
    }
}
