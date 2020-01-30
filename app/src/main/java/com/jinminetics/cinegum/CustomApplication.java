package com.jinminetics.cinegum;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;

import com.jinminetics.cinegum.utils.AppMethods;
import com.jinminetics.cinegum.utils.MemoryLeakUtils;
import com.jinminetics.cinegum.utils.StaticMethods;

import androidx.multidex.MultiDexApplication;
import okhttp3.OkHttpClient;


public class CustomApplication extends MultiDexApplication {
    private static final String TAG = "CustomApplication";
    public static Activity mActivity;
    public NetworkReceiver mNetworkReceiver;
    private static OkHttpClient okHttpClient;
    private static CustomApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        okHttpClient = new OkHttpClient.Builder()/*.addNetworkInterceptor(new StethoInterceptor())*/.build();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //network receiver
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mActivity = activity;
                StaticMethods.log(TAG, "onActivityCreated", mActivity.getClass().getSimpleName());
                mNetworkReceiver = new NetworkReceiver();
                //Icepick.restoreInstanceState(activity, bundle);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
                StaticMethods.log(TAG, "onActivityStarted", mActivity.getClass().getSimpleName());
                try{
                    registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = activity;
                StaticMethods.log(TAG, "onActivityResumed", mActivity.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                StaticMethods.log(TAG, "onActivityPaused", mActivity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                StaticMethods.log(TAG, "onActivityStopped", mActivity.getClass().getSimpleName());
                try{
                    unregisterReceiver(mNetworkReceiver);
                    Log.d("MyApplicationTest", "onActivityStopped: un-registered");
                }catch (Exception e){
                    Log.d("MyApplicationTest", "onActivityStopped: un-register error: "+e.getMessage());
                }

                AppMethods.saveCache(mActivity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                //Icepick.saveInstanceState(activity, bundle);
            }


            @Override
            public void onActivityDestroyed(Activity activity) {
                StaticMethods.log(TAG, "onActivityDestroyed", mActivity.getClass().getSimpleName());
                Log.d(TAG, "Cleaning up after the Android framework");
                MemoryLeakUtils.clearNextServedView(CustomApplication.this);
            }
        });
    }


    @NonNull
    public static CustomApplication get() {
        return INSTANCE;
    }

    public static CustomApplication getPhotoApp() {
        return INSTANCE;
    }

    public Context getContext() {
        return INSTANCE.getContext();
    }

    /**
     * Determines whether this is a release build.
     *
     * @return true if this is a release build, false otherwise.
     */
    public static boolean isRelease() {
        return !BuildConfig.DEBUG || BuildConfig.BUILD_TYPE.toLowerCase().equals("release");
    }
}
