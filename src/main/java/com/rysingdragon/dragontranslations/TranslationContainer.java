package com.rysingdragon.dragontranslations;

import org.spongepowered.api.plugin.PluginContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Translations from all plugins will be stored here.
public final class TranslationContainer {

    private static Map<PluginContainer, Translatable> translatables = new HashMap<>();

    //Set the translation type to use.
    public static void setTranslation(PluginContainer container, Translatable translatable) {
        translatables.put(container, translatable);
    }

    //Get translations from a plugin.
    public static Optional<Translatable> getTranslatable(PluginContainer container) {
        if (translatables.containsKey(container)) {
            return Optional.of(translatables.get(container));
        }
        return Optional.empty();
    }
}
