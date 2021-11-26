package me.swipez.instanceditemsapi;

import me.swipez.instanceditemsapi.annotations.DeclaredModule;
import me.swipez.instanceditemsapi.module.ItemModule;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InstancedItemsRegistry {

    private static HashMap<String, Class<? extends InstancedItem>> customItemsRegistry = new HashMap<>();
    private static HashMap<String, InstancedItem> cachedCustomItems = new HashMap<>();
    private static HashMap<String, Set<UUID>> uuidsOfType = new HashMap<>();

    public static void register(Class<? extends InstancedItem> registeredCustomItem, String identifier){
        customItemsRegistry.put(identifier, registeredCustomItem);

        List<Class<? extends ItemModule>> foundDeclaredModuleClasses = new ArrayList<>();

        // Find the methods annotated as declared modules and add them to the list of module classes in the instanced item.
        Arrays.stream(registeredCustomItem.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(DeclaredModule.class)) {
                Annotation annotation = method.getAnnotation(DeclaredModule.class);
                DeclaredModule declaredModule = (DeclaredModule) annotation;
                foundDeclaredModuleClasses.add(declaredModule.moduleClass());
            }
        });
        InstancedItem instancedItem = null;
        try {
            instancedItem = registeredCustomItem.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Adds all found module classes (Unregistered and in class form)
        for (Class<? extends ItemModule> moduleClass : foundDeclaredModuleClasses) {
            instancedItem.addModuleClass(moduleClass);
        }

        // Adds instance
        InstancedItemsRegistry.addCachedCustomItem(instancedItem);
    }

    public static HashMap<String, Class<? extends InstancedItem>> getCustomItemsRegistry(){
        return customItemsRegistry;
    }

    public static void addCachedCustomItem(InstancedItem instancedItem){
        cachedCustomItems.put(instancedItem.getIdentifier(), instancedItem);
    }

    public static InstancedItem getCachedInstance(String identifier){
        return cachedCustomItems.get(identifier);
    }

    public static String getIdentifier(Class<? extends InstancedItem> customItemClass){
        for (Map.Entry<String, Class<? extends InstancedItem>> customItemClasses : customItemsRegistry.entrySet()){
            if (customItemClasses.getValue().equals(customItemClass)){
                return customItemClasses.getKey();
            }
        }
        return null;
    }

    public static void addUUIDToType(UUID uuid, String type){
        Set<UUID> emptySet = new HashSet<>();
        uuidsOfType.putIfAbsent(type, emptySet);
        Set<UUID> currentSet = uuidsOfType.get(type);
        currentSet.add(uuid);
        uuidsOfType.put(type, currentSet);
    }

    public static boolean isOfType(ItemStack itemStack, String type){
        UUID uuid = getItemUUID(itemStack);
        if (uuid == null){
            return false;
        }
        if (uuidsOfType.get(type) == null){
            return false;
        }
        return uuidsOfType.get(type).contains(uuid);
    }

    private static UUID getItemUUID(ItemStack itemStack){
        if (itemStack == null){
            return null;
        }
        if (!itemStack.hasItemMeta()){
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(new NamespacedKey(InstancedItemsAPI.getPlugin(), "customItemUUID"), PersistentDataType.STRING)){
            return null;
        }
        return UUID.fromString(persistentDataContainer.get(new NamespacedKey(InstancedItemsAPI.getPlugin(), "customItemUUID"), PersistentDataType.STRING));
    }
}
