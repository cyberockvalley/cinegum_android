package com.jinminetics.views;

import android.content.Context;
import android.graphics.Typeface;

public abstract class Utils {
    public static Typeface getTypeFace(Context context, String fontName) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/"+fontName);
    }
}
