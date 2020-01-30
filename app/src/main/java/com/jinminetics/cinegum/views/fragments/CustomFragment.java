package com.jinminetics.cinegum.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
