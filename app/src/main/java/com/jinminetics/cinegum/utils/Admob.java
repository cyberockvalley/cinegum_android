package com.jinminetics.cinegum.utils;

import com.google.android.gms.ads.AdSize;

public class Admob {
    public static final String APP_ID_SAMPLE = "ca-app-pub-3940256099942544~3347511713";
    public static final String BANNER_1_UNIT_ID_SAMPLE = "ca-app-pub-3940256099942544/6300978111";
    public static final String WALL_1_UNIT_ID_SAMPLE = "ca-app-pub-3940256099942544/1033173712";
    public static final String REWARD_1_UNIT_ID_SAMPLE = "ca-app-pub-3940256099942544/5224354917";

    public static final String APP_ID = "ca-app-pub-9647365096448221~8136710741";
    public static final String BANNER_1_UNIT_ID = BANNER_1_UNIT_ID_SAMPLE;//"ca-app-pub-9647365096448221/1188159014";
    public static final String WALL_1_UNIT_ID = WALL_1_UNIT_ID_SAMPLE;//"ca-app-pub-9647365096448221/5501495461";
    public static final String REWARD_1_UNIT_ID = REWARD_1_UNIT_ID_SAMPLE;//"ca-app-pub-9647365096448221/1431701590";

    public static final AdSize BANNER_SIZE_320_BY_50 = AdSize.BANNER;
    public static final AdSize BANNER_SIZE_320_BY_100 = AdSize.LARGE_BANNER;
    public static final AdSize BANNER_SIZE_300_BY_250 = AdSize.MEDIUM_RECTANGLE;
    public static final AdSize BANNER_SIZE_468_BY_60 = AdSize.FULL_BANNER;
    public static final AdSize BANNER_SIZE_970_BY_90 = AdSize.LEADERBOARD;

    private static Admob instance;

    private Admob() {

    }

    public static Admob getInstance() {
        if(instance == null) {
            instance = new Admob();
        }
        return instance;
    }
}
