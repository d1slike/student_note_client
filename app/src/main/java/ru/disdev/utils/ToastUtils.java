package ru.disdev.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Dislike on 30.07.2016.
 */
public class ToastUtils {
    public static void showShortTimeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
