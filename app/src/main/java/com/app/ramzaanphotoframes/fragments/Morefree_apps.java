package com.app.ramzaanphotoframes.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.App;

import java.util.ArrayList;

public class Morefree_apps extends Fragment{

    private ArrayList<App> apps_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apps_list = getArguments().getParcelableArrayList("appslist");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_morefree_apps, container, false);
    }

}
