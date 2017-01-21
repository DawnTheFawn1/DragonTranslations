package com.rysingdragon.dragontranslations;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.translation.locale.Locales;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Plugin(
        id = "dragontranslations",
        version = "1.0.1",
        name = "DragonTranslations",
        description = "Resource for creating translations in Sponge plugins",
        authors = {
                "RysingDragon"
        }
)
public class DragonTranslations {

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