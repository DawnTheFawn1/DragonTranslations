package com.rysingdragon.dragontranslations.data;

import com.rysingdragon.dragontranslations.DragonTranslations;
import com.rysingdragon.dragontranslations.exceptions.InvalidFileException;
import com.rysingdragon.dragontranslations.exceptions.InvalidLocaleException;
import com.rysingdragon.dragontranslations.Translator;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class AssetTranslator implements Translator {

    private Map<String, Map<String, Text>> translations;
    private Locale defaultLocale;

    public AssetTranslator(Object plugin, Locale defaultLocale) {
        this.translations = new HashMap<>();
        this.defaultLocale = defaultLocale;
        PluginContainer container = Sponge.getPluginManager().fromInstance(plugin)
                .orElseThrow(() -> new IllegalArgumentException("You must pass in a valid plugin instance"));
        TranslationContainer.setTranslator(container, this);
    }

    //Adds translations for a Locale using an asset file with .lang extension
    public void addTranslation(Locale locale, Asset asset) {
        if (!asset.getFileName().endsWith(".lang")) {
            try {
                throw new InvalidFileException("Asset provided must be a .lang file, Found: " + asset.getFileName());
            } catch (InvalidFileException e) {
                e.printStackTrace();
            }
        }

        if (!DragonTranslations.getAllMinecraftLocales().contains(locale)) {
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

    //Load all the locales from the plugin assets
    public void loadLocales(Object plugin, String langDirName) {
        try {
            Asset langDirAsset = Sponge.getAssetManager().getAsset(plugin, langDirName)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid directory provided"));
            Map<String, String> env = new HashMap<>();
            String[] array = langDirAsset.getUrl().toURI().toString().split("!");
            FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            Path langDir = fs.getPath(array[1]);

            Files.walk(langDir).filter(child -> !Files.isDirectory(child)).forEach(child -> {
                String fileName = child.getFileName().toString().replace(".lang", "");

                String lang = fileName.substring(0, fileName.indexOf("_"));
                String country = fileName.substring(fileName.indexOf("_") + 1);
                Optional<Asset> optLangAsset = Sponge.getAssetManager().getAsset(plugin, langDirName + fileName + ".lang");
                optLangAsset.ifPresent(langAsset -> this.addTranslation(new Locale(lang, country), langAsset));
            });
            fs.close();
        } catch (IOException | URISyntaxException e) {
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
