package br.com.raspemania.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPrefHelper {

    public static void setSharedOBJECT(Context context, String key, Object value) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static <T> T getSharedOBJECT(Context context, String key, Class<T> clazz) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, null);
        return new Gson().fromJson(json, clazz);
    }

    public static void clearShared(Context context){
        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
