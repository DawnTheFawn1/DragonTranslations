package com.rysingdragon.dragontranslations;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.translation.locale.Locales;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.inject.Inject;

@Plugin(
        id = "dragontranslations",
        version = "1.0.0",
        name = "DragonTranslations",
        description = "Resource for creating translations in Sponge plugins",
        authors = {
                "RysingDragon"
        }
)
public class DragonTranslations {

    @Inject
    public PluginContainer PLUGIN_CONTAINER;

    public static DragonTranslations INSTANCE;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        INSTANCE = this;
    }

    public static PluginContainer getPluginContainer() {
        return DragonTranslations.INSTANCE.PLUGIN_CONTAINER;
    }

    public static Map<Locale, String> getAllMinecraftLocales() {
        Map<Locale, String> locales = new HashMap<>();
        Field[] fields = Locales.class.getFields();
        for (Field field : fields) {
            if (Locale.class.isAssignableFrom(field.getType()) && !field.getName().equalsIgnoreCase("DEFAULT")) {
                try {
                    Locale locale = (Locale) field.get(null);
                    locales.put(locale, locale.getCountry());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return locales;
    }
}