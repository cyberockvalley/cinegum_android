package com.jinminetics.cinegum.utils;

import android.app.Activity;

public class AppMethods {
    public static void saveCache(Activity activity) {

    }

    public static String aboluteUrl(String url) {
        if(url == null) return null;
        if(url.startsWith("/")) {
            return Constants.API_URL + url.substring(1);

        } else if(url.startsWith("http")) {
            return url;

        } else {
            return Constants.API_URL + url;
        }
    }
}
