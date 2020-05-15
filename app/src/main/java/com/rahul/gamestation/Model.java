package com.rahul.gamestation;

public class Model {
    public String mTitle;
    public String mImage;
    public  String mVideo;

    public Model() {

    }

    public Model(String mTitle, String mImage, String mVideo) {
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mVideo = mVideo;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmVideo() {
        return mVideo;
    }

    public void setmVideo(String mVideo) {
        this.mVideo = mVideo;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}