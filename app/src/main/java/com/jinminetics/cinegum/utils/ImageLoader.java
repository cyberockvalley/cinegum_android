package com.jinminetics.cinegum.utils;

import android.view.View;
import android.widget.ImageView;

import com.jinminetics.cinegum.NetworkReceiver;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ImageLoader {
    private ImageView imageView;
    private String path;
    private View progressView;
    private int placeHolder = -1;
    private int retries;
    public ImageLoader(ImageView imageView, String path, View progressView, int placeHolder, int retries) {
        this.imageView = imageView;
        this.path = path;
        this.progressView = progressView;
        this.placeHolder = placeHolder;
        this.retries = retries;

    }

    public ImageLoader(ImageView imageView, String path, View toggler, int placeHolder) {
        this.imageView = imageView;
        this.path = path;
        this.progressView = toggler;
        this.placeHolder = placeHolder;

    }

    public ImageLoader(ImageView imageView, String path, View toggler) {
        this.imageView = imageView;
        this.path = path;
        this.progressView = toggler;

    }

    public ImageLoader(ImageView imageView, String path, int placeHolder) {
        this.imageView = imageView;
        this.path = path;
        this.placeHolder = placeHolder;

    }

    public ImageLoader(ImageView imageView, String path) {
        this.imageView = imageView;
        this.path = path;

    }

    public void load() {
        Picasso.get().load(path).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if(progressView != null) progressView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                if(progressView != null) progressView.setVisibility(View.GONE);
                if(placeHolder > -1)Picasso.get().load(placeHolder).into(imageView);
                if(retries > 0) {
                    final String key = StaticMethods.genUniqueTag(8)
                            +"_"+String.valueOf(Calendar.getInstance().getTimeInMillis());
                    NetworkReceiver.onInternetConnectionDetectedTasks.put(key, new NetworkReceiver.OnInternetConnectionDetectedTask() {
                        @Override
                        public void onConnected() {
                            load();
                        }
                    });
                    retries--;
                }
            }
        });
    }
}
