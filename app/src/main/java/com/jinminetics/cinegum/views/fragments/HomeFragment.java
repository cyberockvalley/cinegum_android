package com.jinminetics.cinegum.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.providers.App;
import com.jinminetics.cinegum.utils.Admob;
import com.jinminetics.cinegum.utils.StaticMethods;
import com.jinminetics.cinegum.views.activities.EditProfileActivity;
import com.jinminetics.views.JTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private CircleImageView mProfileImage;
    private ProgressBar mProfileImageProgressBar;
    private JTextView mUsername, mEmail, mEditProfile;
    private LinearLayout mAdmobBannerContainer;

    private AdView admobBanner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    @Override
    public void init() {
        super.init();
        mContext = getContext();
        mActivity = getActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mProfileImage = findViewById(R.id.profileImage);
        mProfileImageProgressBar = findViewById(R.id.profileImageProgress);
        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mEditProfile = findViewById(R.id.editProfile);
        mEditProfile.setOnClickListener(this);
        mAdmobBannerContainer = findViewById(R.id.admobBannerContainer);
        admobBanner = new AdView(mContext);
        admobBanner.setAdSize(Admob.BANNER_SIZE_320_BY_50);
        admobBanner.setAdUnitId(Admob.BANNER_1_UNIT_ID);
        mAdmobBannerContainer.addView(admobBanner);
        admobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                loadBannerAd();
            }
        });
        loadBannerAd();
    }

    private void loadBannerAd() {
        admobBanner.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));
        if(v == mEditProfile) {
            startActivity(new Intent(mContext, EditProfileActivity.class));
            mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_scale);
            return;
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.rate:
                StaticMethods.rate(mContext);
                return false;
            case R.id.share:
                StaticMethods.shareit(mContext);
                return false;
            case R.id.feedback:
                StaticMethods.feedback(mContext);
                return false;
            case R.id.contactUs:
                StaticMethods.contactUs(mContext);
                return false;
            case R.id.logout:
                App.getInstance(mContext).logout();
                return false;
        }
        return true;
    }
}
