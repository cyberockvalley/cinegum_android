package com.jinminetics.cinegum.models;

public class Video {
    private String thumb;
    private String title;
    private int reward;

    public Video(){

    }

    public Video(String thumb, String title, int reward) {
        this.thumb = thumb;
        this.title = title;
        this.reward = reward;
    }

    public String getThumb() {
        return thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReward() {
        return reward;
    }
}
