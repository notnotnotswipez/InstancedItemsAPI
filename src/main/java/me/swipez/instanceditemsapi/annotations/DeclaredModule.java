package me.swipez.instanceditemsapi.annotations;

import me.swipez.instanceditemsapi.module.ItemModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeclaredModule {
    Class<? extends ItemModule> moduleClass();
}
