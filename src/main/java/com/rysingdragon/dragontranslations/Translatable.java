package com.rysingdragon.dragontranslations;

import org.spongepowered.api.text.Text;

import java.util.Locale;

public interface Translatable {

    //Get the translated text
    Text translate(Locale locale, String key);

    //The default locale to use if a translation is not found for a user's language
    Locale getDefaultLocale();
}
