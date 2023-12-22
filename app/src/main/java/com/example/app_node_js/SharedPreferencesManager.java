package com.example.app_node_js;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.UserHandle;

import androidx.annotation.NonNull;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "user";

    //,todo--@NonNull--
    // indicating that this parameter is expected to be non-null when calling the saveUserData method
    public static void saveUserData  (@NonNull Context context, String username, String email, String avatar, String token) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username",username);
        editor.putString("email",email);
        editor.putString("avatar",avatar);
        editor.putString("token",token);

        editor.apply();

    }

    public static String getUsername(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString("username","");
    }

    public static String getEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString("email","");
    }

    public static String getAvatar(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString("avatar","");
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public static void clearUserData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

}
