package com.jinminetics.cinegum.utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jinminetics.cinegum.NetworkReceiver;
import com.jinminetics.cinegum.providers.App;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private InterstitialAd wallAd;
    private RewardedVideoAd rewardedVideoAd;
    private RewardAdListener rewardedVideoAdListener;
    private long MINIMUM_TIME_ELAPSED_BEFORE_RETRY = 20 * 1000;

    private long lastWallAdDisplayTime;

    private AdListener wallAdListener;
    private RewardAdListener rewardAdListener;
    private AtomicBoolean alertOnRewardVideoAvailable = new AtomicBoolean(false);
    private AtomicBoolean alertOnWallAdAvailable = new AtomicBoolean(false);

    private Admob() {
    }
    private Admob(Context context) {
        initWallAd(context);
        initRewardVideoAd(context);
    }

    private void initWallAd(Context context) {
        wallAd = new InterstitialAd(context);
        wallAd.setAdUnitId(WALL_1_UNIT_ID);
        wallAd.setAdListener(new com.google.android.gms.ads.AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(alertOnWallAdAvailable.get()) {
                    if(wallAdListener != null) {
                        wallAdListener.onAvailable();
                    }
                    alertOnWallAdAvailable.set(false);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if(!wallAd.isLoading()) {
                    if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                        final String key = StaticMethods.genUniqueTag(8)
                                +"_"+String.valueOf(Calendar.getInstance().getTimeInMillis());
                        NetworkReceiver.onInternetConnectionDetectedTasks.put(key, new NetworkReceiver.OnInternetConnectionDetectedTask() {
                            @Override
                            public void onConnected() {
                                loadWallAd();
                            }
                        });

                    } else if(errorCode == AdRequest.ERROR_CODE_NO_FILL || errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                loadWallAd();
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(timerTask, MINIMUM_TIME_ELAPSED_BEFORE_RETRY);
                    }
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadWallAd();
            }
        });
        loadWallAd();
    }

    private void initRewardVideoAd(Context context) {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.setUserId(String.valueOf(App.getInstance(context).getUser().getId()));
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if(alertOnRewardVideoAvailable.get()) {
                    if(rewardedVideoAdListener != null) {
                        rewardedVideoAdListener.onAvailable();
                    }
                    alertOnRewardVideoAvailable.set(false);
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                if(rewardedVideoAdListener != null) rewardedVideoAdListener.onRewarded(rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    final String key = StaticMethods.genUniqueTag(8)
                            +"_"+String.valueOf(Calendar.getInstance().getTimeInMillis());
                    NetworkReceiver.onInternetConnectionDetectedTasks.put(key, new NetworkReceiver.OnInternetConnectionDetectedTask() {
                        @Override
                        public void onConnected() {
                            loadRewardedVideoAd();
                        }
                    });

                } else if(errorCode == AdRequest.ERROR_CODE_NO_FILL || errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            loadRewardedVideoAd();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask, MINIMUM_TIME_ELAPSED_BEFORE_RETRY);
                }
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        loadRewardedVideoAd();
    }

    public static Admob getInstance(Context context) {
        if(instance == null) {
            instance = new Admob(context);
        }
        return instance;
    }

    public void showWallAd(AdListener listener) {
        if(wallAd.isLoaded()) {
            wallAd.show();
            lastWallAdDisplayTime = System.currentTimeMillis();

        } else {
            if(listener != null) {
                wallAdListener = listener;
                alertOnWallAdAvailable.set(true);
                listener.onEmpty();
                if(!wallAd.isLoading())loadWallAd();
            }
        }
    }

    public void showRewardVideoAd(RewardAdListener listener) {
        rewardedVideoAdListener = listener;
        if(rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();

        } else {
            if(listener != null) {
                alertOnRewardVideoAvailable.set(true);
                listener.onEmpty();
            }
        }
    }

    public long timeElapsedSinceLastWallAd() {
        return System.currentTimeMillis() - lastWallAdDisplayTime;
    }

    public InterstitialAd getWallAd() {
        return wallAd;
    }

    public RewardedVideoAd getRewardedVideoAd() {
        return rewardedVideoAd;
    }

    public void loadWallAd() {
        wallAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(REWARD_1_UNIT_ID,
                new AdRequest.Builder().build());
    }

    public interface AdListener {
        void onAvailable();
        void onEmpty();
    }

    public interface RewardAdListener extends AdListener {
        void onRewarded(int reward);
    }

}
