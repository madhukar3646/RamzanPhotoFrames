package com.app.ramzaanphotoframes.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FramesModel {

    @SerializedName("portrait")
    @Expose
    private List<String> portrait = null;
    @SerializedName("landscape")
    @Expose
    private List<String> landscape = null;

    public List<String> getPortrait() {
        return portrait;
    }

    public void setPortrait(List<String> portrait) {
        this.portrait = portrait;
    }

    public List<String> getLandscape() {
        return landscape;
    }

    public void setLandscape(List<String> landscape) {
        this.landscape = landscape;
    }
}
