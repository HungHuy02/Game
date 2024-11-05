package com.huy.game.android.utils;

import java.util.regex.Pattern;

public class EmailValidation {

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile("^(.+)@(\\S+)$")
            .matcher(emailAddress)
            .matches();
    }
}
