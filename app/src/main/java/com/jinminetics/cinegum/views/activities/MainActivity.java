package com.jinminetics.cinegum.views.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.utils.Admob;
import com.jinminetics.cinegum.views.fragments.CustomFragment;
import com.jinminetics.cinegum.views.fragments.HomeFragment;
import com.jinminetics.cinegum.views.fragments.UploadFragment;
import com.jinminetics.cinegum.views.fragments.VideosFragment;
import com.jinminetics.cinegum.views.fragments.WithdrawFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<CustomFragment> frags;
    public static int IMAGE_SELECT_REQ_FOR_ADD_FRAG = 300;

    private Context mContext;
    private Activity mActivity;

    private static int currentFrag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        mActivity = (Activity)mContext;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        Admob.getInstance(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        init();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                setFrag(0);
                return true;
            case R.id.navigation_videos:
                setFrag(1);
                return true;
            case R.id.navigation_upload:
                setFrag(2);
                return true;
            case R.id.navigation_withdraw:
                setFrag(3);
                return true;
        }
        return false;
    }

    private void init() {
        if(frags == null) {
            frags = new ArrayList<>();
            frags.add(new HomeFragment());
            frags.add(new VideosFragment());
            frags.add(new UploadFragment());
            frags.add(new WithdrawFragment());
            setFrag(0);
        }
    }

    public void setFrag(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, frags.get(index));
        transaction.commit();
        currentFrag = index;
    }

    @Override
    protected void onResume() {
        Admob.getInstance(this).getRewardedVideoAd().resume(this);
        if(!Admob.getInstance(this).getRewardedVideoAd().isLoaded())
            Admob.getInstance(this).loadRewardedVideoAd();

        if(!Admob.getInstance(this).getWallAd().isLoaded())
            Admob.getInstance(this).loadWallAd();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Admob.getInstance(this).getRewardedVideoAd().pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Admob.getInstance(this).getRewardedVideoAd().destroy(this);
        super.onDestroy();
    }
}
