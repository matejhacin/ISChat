package com.matejhacin.ispogovor.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matejhacin on 09/01/16.
 */
public class LoginManager {

    /*
    Variables
     */

    private static LoginManager ourInstance;

    private SharedPreferences sharedPreferences;

    /*
    Singleton
     */

    public static LoginManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new LoginManager(context);
        }

        return ourInstance;
    }

    /*
    Constructor
     */

    private LoginManager(Context context) {
        sharedPreferences = context.getSharedPreferences(LoginManager.class.getName(), 0);
    }

    /*
    Methods
     */

    public void saveUser(String username, String password) {
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("password", password).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }

    public String getAuthorizationHeader() {
        return String.format("%s:%s", getUsername(), getPassword());
    }
}
