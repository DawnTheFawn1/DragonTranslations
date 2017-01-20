package com.rysingdragon.dragontranslations.data;

import com.rysingdragon.dragontranslations.DragonTranslations;
import com.rysingdragon.dragontranslations.InvalidLocaleException;
import com.rysingdragon.dragontranslations.Translator;

import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AssetTranslator implements Translator {

    private Map<String, Map<String, Text>> translations;
    private Locale defaultLocale;

    public AssetTranslator(Locale defaultLocale) {
        this.translations = new HashMap<>();
        this.defaultLocale = defaultLocale;
    }

    //Adds translations for a Locale using the specified Asset
    public void addTranslation(Locale locale, Asset asset) {
        if (!DragonTranslations.getAllMinecraftLocales().keySet().contains(locale)) {
            try {
                throw new InvalidLocaleException("Provided Locale not available in Minecraft");
            } catch (InvalidLocaleException e) {
                e.printStackTrace();
            }
        }
        Map<String, Text> translationKeys = new HashMap<>();
        this.translations.put(locale.toString(), translationKeys);
        try {
            List<String> lines = asset.readLines();
            lines.forEach(line -> {
                int translationPos = line.indexOf("=") + 1;
                String key = line.substring(0, translationPos - 1);
                String rawValue = line.substring(translationPos);
                Text serialized = TextSerializers.FORMATTING_CODE.deserialize(rawValue);
                translationKeys.put(key, serialized);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Translate into the selected locale, if none is found, use the default locale. If there's still no translation found, return the key provided
    @Override
    public Text translate(Locale locale, String key) {
        if (this.translations.containsKey(locale.toString())) {
            Map<String, Text> translationKeys = this.translations.get(locale.toString());
            if (translationKeys.containsKey(key)) {
                return translationKeys.get(key);
            }
        } else if (this.translations.containsKey(getDefaultLocale().toString())) {
            Map<String, Text> translationKeys = this.translations.get(locale.toString());
            if (translationKeys.containsKey(key)) {
                return translationKeys.get(key);
            }
        }
        return Text.of(key);
    }

    @Override
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }
}
