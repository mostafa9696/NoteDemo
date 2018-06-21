package com.example.mostafahussien.rx_demo.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mostafa Hussien on 04/06/2018.
 */

public class PrefsUtils {   // stores and retrieves the API Key that needs to be sent in every HTTP call as Authorization header field.
    public PrefsUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }

    public static String getApiKey(Context context) {
        return getSharedPreferences(context).getString("API_KEY", null);
    }
}
