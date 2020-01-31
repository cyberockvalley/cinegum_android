package com.jinminetics.cinegum.views.adapters;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jinminetics.cinegum.R;
import com.jinminetics.cinegum.models.Video;
import com.jinminetics.views.JTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoListAdapter extends ArrayAdapter<Video> {
    private Context context;
    private int layout;
    private List<Video> videos;
    private LayoutInflater inflater;
    public VideoListAdapter(@NonNull Context context, int resource, @NonNull List<Video> objects) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        videos = objects;
        inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        ImageView thumb;
        JTextView title;
        JTextView reward;
        public ViewHolder(View view) {
            thumb = view.findViewById(R.id.thumb);
            title = view.findViewById(R.id.title);
            reward = view.findViewById(R.id.reward);
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView != null) {
            viewHolder = (ViewHolder)convertView.getTag();

        } else {
            convertView = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        Video video = getItem(position);

        Picasso.get().load(video.getThumb())
                .into(viewHolder.thumb);

        viewHolder.title.setText(video.getTitle());
        viewHolder.reward.setText(String.format("+ %d coins", video.getReward()));

        return convertView;
    }

    @Override
    public int getCount() {
        return videos.size();
    }
}
