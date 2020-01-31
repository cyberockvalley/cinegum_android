package com.jinminetics.cinegum.views.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.jinminetics.cinegum.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class YoutubeActivity extends AppCompatActivity implements YouTubePlayerListener {
    private static final String TAG = YoutubeActivity.class.getSimpleName();
    private static final int SEEK_RATE = 10;//10 seconds

    private Context mContext;
    private Activity mActivity;

    private YouTubePlayerView mPlayerView;
    private String videoCode = "VNqNnUJVcVs";

    private YouTubePlayerTracker tracker;
    private PlayerUiController controller;
    private YouTubePlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mContext = this;
        mActivity = (Activity) mContext;
        init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_scale, R.anim.slide_out_to_right);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.release();
    }

    private void init() {
        mPlayerView = findViewById(R.id.player);
        getLifecycle().addObserver(mPlayerView);
        controller = mPlayerView.getPlayerUiController();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            controller.setCustomAction1(getDrawable(R.drawable.ic_rotate_left_black_24dp), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekBack();
                }
            });

            controller.setCustomAction2(getDrawable(R.drawable.ic_rotate_right_black_24dp), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekForward();
                }
            });

        } else {
            controller.setCustomAction1(getResources().getDrawable(R.drawable.ic_rotate_left_black_24dp), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekBack();
                }
            });

            controller.setCustomAction2(getResources().getDrawable(R.drawable.ic_rotate_right_black_24dp), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekForward();
                }
            });
        }

        mPlayerView.getYouTubePlayerWhenReady(player -> {
            this.player = player;
            YouTubePlayerUtils.loadOrCueVideo(this.player, getLifecycle(), videoCode, 0);
            tracker = new YouTubePlayerTracker();
            this.player.addListener(tracker);
            this.player.addListener(this);
        });
    }

    private void seekBack() {
        float seekTime = tracker.getCurrentSecond() - SEEK_RATE;
        seek(seekTime);
    }

    private void seekForward() {
        float seekTime = tracker.getCurrentSecond() + SEEK_RATE;
        seek(seekTime);
    }

    private void seek(float time) {
        if(player != null) {
            player.seekTo(time);
        }
    }

    @Override
    public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

    }

    @Override
    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

    }

    @Override
    public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

    }

    @Override
    public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

    }

    @Override
    public void onReady(@NotNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {

    }

    @Override
    public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

    }

    @Override
    public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

    }

    @Override
    public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

    }
}
