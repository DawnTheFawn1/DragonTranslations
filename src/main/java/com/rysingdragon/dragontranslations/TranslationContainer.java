package com.rysingdragon.dragontranslations;

import org.spongepowered.api.plugin.PluginContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Translations from all plugins will be stored here.
public final class TranslationContainer {

    private static Map<PluginContainer, Translator> translators = new HashMap<>();

    //Set the translation type to use.
    public static void setTranslator(PluginContainer container, Translator translatable) {
        translators.put(container, translatable);
    }

    //Get translations from a plugin.
    public static Optional<Translator> getTranslator(PluginContainer container) {
        return Optional.ofNullable(translators.get(container));
    }
}
