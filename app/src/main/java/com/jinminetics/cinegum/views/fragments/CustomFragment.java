package com.jinminetics.cinegum.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.providers.App;
import com.jinminetics.cinegum.utils.StaticMethods;
import com.jinminetics.cinegum.views.activities.AboutUsActivity;
import com.jinminetics.cinegum.views.activities.MainActivity;

public abstract class CustomFragment extends Fragment {
    public Context mContext = getContext();
    public Activity mActivity = getActivity();

    public View view;
    private boolean fragVisible;

    public void setFrag(int index) {
        ((MainActivity)mActivity).setFrag(index);
    }

    public  <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    public void init(){}

    public boolean isFragVisible() {
        return fragVisible;
    }

    public void setFragVisible(boolean fragVisible) {
        this.fragVisible = fragVisible;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            case R.id.aboutUs:
                StaticMethods.goTo(mContext, AboutUsActivity.class, false);
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
