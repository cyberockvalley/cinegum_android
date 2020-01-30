package com.jinminetics.cinegum.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.views.activities.EditProfileActivity;
import com.jinminetics.cinegum.views.activities.LoginActivity;
import com.jinminetics.cinegum.views.activities.RegisterActivity;
import com.jinminetics.views.JTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private CircleImageView mProfileImage;
    private ProgressBar mProfileImageProgressBar;
    private JTextView mUsername, mEmail, mEditProfile, mPhotoCount;
    private GridView mPhotoGrid;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyMessage;
    private JTextView mTryAgain, mEmptyText;
    private AtomicBoolean loadingPhotos = new AtomicBoolean(false);
    //private List<Photo> mPhotos = new ArrayList<>();
    //private PhotoGridAdapter mPhotoGridAdapter;
    private ProgressBar mFooterProgress;
    private boolean hasNext = true;
    private static int page = 1;

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
        mPhotoCount = findViewById(R.id.photoCount);
        mPhotoGrid = findViewById(R.id.gridView);
        mFooterProgress = findViewById(R.id.footer_progress);
        mFooterProgress.setVisibility(View.GONE);

        mTryAgain = findViewById(R.id.try_again);
        mTryAgain.setOnClickListener(this);
        mUsername.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));
        if(v == mEditProfile) {
            startActivity(new Intent(mContext, EditProfileActivity.class));
            mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_scale);
            return;
        }
        if(v == mUsername) {
            startActivity(new Intent(mContext, LoginActivity.class));
            mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_scale);
            return;
        }
        if(v == mTryAgain) {
            startActivity(new Intent(mContext, RegisterActivity.class));
            mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_scale);
        }
    }
}
