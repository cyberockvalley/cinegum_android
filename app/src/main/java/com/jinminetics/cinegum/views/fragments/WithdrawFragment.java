package com.jinminetics.cinegum.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinminetics.cinegum.R;
import com.jinminetics.views.JTextView;

public class WithdrawFragment extends CustomFragment {
    private static final String TAG = WithdrawFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_withdraw, container, false);
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
    }
}
