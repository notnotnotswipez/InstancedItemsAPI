package me.swipez.instanceditemsapi;

import me.swipez.instanceditemsapi.annotations.DeclaredModule;
import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InstancedItem {

    public Object specificInstance = null;

    private ItemStack baseItem = null;
    private List<Class<? extends ItemModule>> finalModules = new ArrayList<>();
    private final List<ItemModule> registeredItemModules = new ArrayList<>();
    private final String identifier;
    boolean hasBeenCalled = false;
    public UUID customItemID = null;

    public InstancedItem(String identifier, ItemStack baseItem) {
        this.identifier = identifier;
        this.baseItem = baseItem;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getBaseItem() {
        if (!hasBeenCalled){
            customItemID = UUID.randomUUID();
            InstancedItemsRegistry.addUUIDToType(customItemID, identifier);
            ItemMeta itemMeta = baseItem.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey(InstancedItemsAPI.getPlugin(), "customItemUUID"), PersistentDataType.STRING, customItemID.toString());
            baseItem.setItemMeta(itemMeta);
            hasBeenCalled = true;
        }
        return baseItem;
    }

    public void addModuleClass(Class<? extends ItemModule> module){
        finalModules.add(module);
    }

    // Creates a working and registered instance of the custom item class. With all methods registered.
    public InstancedItem copy(Class<? extends InstancedItem> classedInstance) {
        InstancedItem copy = new InstancedItem(identifier, baseItem);

        for (Class<? extends ItemModule> moduleClass : finalModules){

            copy.addModuleClass(moduleClass);

            Arrays.stream(getClass().getDeclaredMethods()).forEach(method -> {
                if (method.isAnnotationPresent(DeclaredModule.class)){
                    Annotation annotation = method.getAnnotation(DeclaredModule.class);
                    DeclaredModule declaredModule = (DeclaredModule) annotation;
                    if (moduleClass.getName().equals(declaredModule.moduleClass().getName()) ){
                        ItemModule instanced = null;
                        try {
                            instanced = moduleClass.getConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        instanced.setMainItem(copy);
                        instanced.setMethod(method);
                        copy.registeredItemModules.add(instanced);
                    }
                }
            });
        }
        try {
            copy.specificInstance = classedInstance.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        copy.register();
        return copy;
    }

    public void register(){
        for (ItemModule itemModule : registeredItemModules){
            InstancedItemsAPI.getPlugin().getServer().getPluginManager().registerEvents(itemModule, InstancedItemsAPI.getPlugin());
        }
    }

    // Doesnt work LMAO, in all honesty I dont quite see a usecase where an item would want its listeners unregistered.
    public void unregister(){
        for (ItemModule itemModule : registeredItemModules) {
            Arrays.stream(itemModule.getClass().getDeclaredMethods()).forEach(method -> {
                Bukkit.broadcastMessage(method.getName());
                if (method.isAnnotationPresent(EventHandler.class)){
                    Bukkit.broadcastMessage(method.getName());
                    for (Parameter parameter : method.getParameters()) {
                        Bukkit.broadcastMessage(parameter.getName());
                        if (parameter.getClass().getGenericSuperclass() instanceof Event){
                            Event event = (Event) parameter.getClass().getGenericSuperclass();
                            event.getHandlers().unregister(itemModule);
                        }
                    }
                }
            });
        }
    }
}
