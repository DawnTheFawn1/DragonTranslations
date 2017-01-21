package com.rysingdragon.dragontranslations.exceptions;

public class InvalidLocaleException extends Exception {

    //Thrown when a locale is inputted that isn't supported by Minecraft.
    public InvalidLocaleException(String message) {
        super(message);
    }
}
