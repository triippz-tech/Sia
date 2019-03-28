package com.trievosoftware.discord.utils;

import org.apache.commons.validator.routines.UrlValidator;

public class Validate {

    public static Pair<Boolean, String> validateUrl (String url)
    {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(url)) {
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "URL is invalid. Please ensure you provide the full url: `https//www.example.com`");
        }
    }

}
