package com.app.ramzaanphotoframes.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App {

    @SerializedName("appname")
    @Expose
    private String appname;
    @SerializedName("appicon")
    @Expose
    private String appicon;
    @SerializedName("appurl")
    @Expose
    private String appurl;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppicon() {
        return appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }
}
