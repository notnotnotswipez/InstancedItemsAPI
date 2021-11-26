package me.swipez.instanceditemsapi.module;

import me.swipez.instanceditemsapi.InstancedItem;
import me.swipez.instanceditemsapi.InstancedItemsAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Method;

public class ItemModule implements Listener {

    private InstancedItem mainItem;
    private Method method;

    public ItemModule(){

    }

    public void setMainItem(InstancedItem mainItem) {
        this.mainItem = mainItem;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public InstancedItem getMainItem() {
        return mainItem;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isItem(ItemStack itemStack){
        if (itemStack == null){
            return false;
        }
        if (!itemStack.hasItemMeta()){
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(new NamespacedKey(InstancedItemsAPI.getPlugin(), "customItemUUID"), PersistentDataType.STRING)){
            return false;
        }
        return persistentDataContainer.get(new NamespacedKey(InstancedItemsAPI.getPlugin(), "customItemUUID"), PersistentDataType.STRING).equals(getMainItem().customItemID.toString());
    }
}
