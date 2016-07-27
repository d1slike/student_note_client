package ru.disdev.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ru.disdev.controllers.AuthController;

/**
 * Created by DisDev on 24.07.2016.
 */
public class SettingsUtil {
    private static final String USER_SETTINGS_FILE_NAME = "setting.xml";

    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(USER_SETTINGS_FILE_NAME, Context.MODE_PRIVATE).edit();
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(USER_SETTINGS_FILE_NAME, Context.MODE_PRIVATE);
    }
}
