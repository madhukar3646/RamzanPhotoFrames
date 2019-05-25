package com.app.ramzaanphotoframes.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaystoreappslistingResponse {

    @SerializedName("apps")
    @Expose
    private List<App> apps = null;

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }
}
