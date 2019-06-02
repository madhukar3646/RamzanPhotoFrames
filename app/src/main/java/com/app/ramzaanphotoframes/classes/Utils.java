package com.app.ramzaanphotoframes.classes;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;


public class Utils {

    public static final String BASE_URL="https://raw.githubusercontent.com/madhukar3646/myplaystoreappslisting/master/";
    public static final String RAMZAANPFS_BASE_URL="https://raw.githubusercontent.com/madhukar3646/PhotoFrameImages/master/RamzanFrames/";
    public static ArrayList<Boolean> checkStatus_ads = new ArrayList<>();
    public static Integer[] positionValues_ads = {2, 7, 10, 15};
    public static String[] positionNames_ads = {"two", "seven", "ten", "fiften"};
    public static int selectpos;

    public static void showInternetToast(Context context)
    {
        Toast.makeText(context,"Please check your internet connection and try again",Toast.LENGTH_SHORT).show();
    }
}
