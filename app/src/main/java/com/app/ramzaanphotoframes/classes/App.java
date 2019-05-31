package com.app.ramzaanphotoframes.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App implements Parcelable {

    @SerializedName("appname")
    @Expose
    private String appname;
    @SerializedName("appicon")
    @Expose
    private String appicon;
    @SerializedName("appurl")
    @Expose
    private String appurl;

    protected App(Parcel in) {
        appname = in.readString();
        appicon = in.readString();
        appurl = in.readString();
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appname);
        dest.writeString(appicon);
        dest.writeString(appurl);
    }
}
