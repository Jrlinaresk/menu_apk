package com.palgao.menu;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by munix on 07/04/16.
 */
public class Preferences {

    public static SharedPreferences getSharedPreferenceManager(Context context) {
        try {
            return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Elimina una preferencia almacenada en SharedPreferences
     *
     * @param context
     * @param key
     */
    public static void deleteSharedPreference(Context context, String key) {
        try {
            getSharedPreferenceManager(context).edit().remove(key).apply();
        } catch (Exception e) {
        }
    }

    /**
     * Elimina todas las Shared Preferences que empiecen con keyStartWith
     *
     * @param context
     * @param keyStartWith
     * @return
     */
    public static void deleteSharedPreferenceByPartialKey(Context context, String keyStartWith) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            Map<String, ?> keys = settings.getAll();
            SharedPreferences.Editor editor = settings.edit();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                if (entry.getKey().startsWith(keyStartWith)) {
                    editor.remove(entry.getKey());
                }
            }
            editor.apply();
        } catch (Exception e) {
        }

    }

    /**
     * Guardar una preferencia
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeSharedPreference(Context context, String key, String value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * Guardar una preferencia
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeSharedPreference(Context context, String key, Boolean value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * Guardar una preferencia
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeSharedPreference(Context context, String key, long value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * Guardar una preferencia
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeSharedPreference(Context context, String key, int value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * Guardar una preferencia
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeSharedPreference(Context context, String key, float value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat(key, value);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * Lee una preferencia
     *
     * @param context
     * @param key
     * @param default_value
     * @return
     */
    public static Boolean readSharedPreference(Context context, String key, Boolean default_value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            return settings.getBoolean(key, default_value);
        } catch (Exception e) {
            return default_value;
        }
    }

    /**
     * Lee una preferencia
     *
     * @param context
     * @param key
     * @param default_value
     * @return
     */
    public static long readSharedPreference(Context context, String key, long default_value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            return settings.getLong(key, default_value);
        } catch (Exception e) {
            return default_value;
        }
    }

    /**
     * Lee una preferencia
     *
     * @param context
     * @param key
     * @param default_value
     * @return
     */
    public static int readSharedPreference(Context context, String key, int default_value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            return settings.getInt(key, default_value);
        } catch (Exception e) {
            return default_value;
        }
    }

    /**
     * Lee una preferencia
     *
     * @param context
     * @param key
     * @param default_value
     * @return
     */
    public static float readSharedPreference(Context context, String key, float default_value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            return settings.getFloat(key, default_value);
        } catch (Exception e) {
            return default_value;
        }
    }

    /**
     * Lee una preferencia
     *
     * @param context
     * @param key
     * @param default_value
     * @return
     */
    public static String readSharedPreference(Context context, String key, String default_value) {
        try {
            SharedPreferences settings = getSharedPreferenceManager(context);
            return settings.getString(key, default_value);
        } catch (Exception e) {
            return default_value;
        }
    }
}
