package com.jinminetics.cinegum.providers;

import android.content.Context;

import com.jinminetics.cinegum.models.User;

public class App {
    private static App instance;
    private Context context;

    private User user;

    private App(Context context) {
        this.context = context;
        user = new User();
    }

    public static App getInstance(Context context) {
        if(instance ==  null) {
            instance = new App(context);
        }
        return instance;
    }
    public void logout() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
