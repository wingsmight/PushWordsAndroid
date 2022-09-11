package com.wingsmight.pushwords.data.stores;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.User;

public class UserStore {
    public static final String PREF_NAME = "UserStore";


    private static UserStore instance;

    private Context context;
    private User user;


    private UserStore(Context context) {
        this.context = context;

        SharedPreferences preferences = context.getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        Gson gson = new Gson();
        String objectAsJson = preferences.getString(UserStore.PREF_NAME, null);
        if (objectAsJson == null || objectAsJson.isEmpty()) {
            user = null;
        } else {
            user = gson.fromJson(objectAsJson, User.class);
        }
    }


    public static synchronized UserStore getInstance(Context context) {
        if (instance == null) {
            synchronized (UserStore.class) {
                if(instance == null) {
                    instance = new UserStore(context);
                }
            }
        }
        return instance;
    }
    public void save() {
        SharedPreferences preferences = context.getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        Gson gson = new Gson();
        String objectAsJson = gson.toJson(user);
        preferencesEditor.putString(PREF_NAME, objectAsJson);
        preferencesEditor.apply();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
