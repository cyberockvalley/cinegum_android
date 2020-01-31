package com.jinminetics.cinegum.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.models.Video;
import com.jinminetics.cinegum.utils.Admob;
import com.jinminetics.cinegum.utils.StaticMethods;
import com.jinminetics.cinegum.views.adapters.VideoListAdapter;
import com.jinminetics.views.JTextView;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = VideosFragment.class.getSimpleName();

    private JTextView mEmptyText, mTryAgain;
    private ListView mListView;
    private ProgressBar mFooterProgress;

    private List<Video> playlist = new ArrayList<>();
    private VideoListAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_videos, container, false);
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

        mEmptyText = findViewById(R.id.empty_text);
        mTryAgain = findViewById(R.id.try_again);
        mListView = findViewById(R.id.listView);
        mFooterProgress = findViewById(R.id.footer_progress);
        listAdapter = new VideoListAdapter(mContext, R.layout.video_list_item, playlist);
        loadVideos();
    }

    private void loadVideos() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Admob.getInstance(mContext).showWallAd(new Admob.AdListener() {
            @Override
            public void onAvailable() {

            }

            @Override
            public void onEmpty() {
                StaticMethods.showSnackbar(mActivity, "No WallAd available now. Try later.", Snackbar.LENGTH_LONG);
            }
        });
    }

    private void showRewardVideo() {
        Admob.getInstance(mContext).showRewardVideoAd(new Admob.RewardAdListener() {
            @Override
            public void onRewarded(int reward) {
                StaticMethods.showAlert(mActivity, null, String.format(getString(R.string.message_after_admob_reward), reward, reward),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showRewardVideo();
                            }
                        });
            }

            @Override
            public void onAvailable() {
                StaticMethods.showSnackbar(mActivity, getString(R.string.admob_videos_availability_message), Snackbar.LENGTH_LONG);
            }

            @Override
            public void onEmpty() {
                StaticMethods.showSnackbar(mActivity, "No video available now. Try later.", Snackbar.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onClick(View v) {
        v.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));

    }
}
