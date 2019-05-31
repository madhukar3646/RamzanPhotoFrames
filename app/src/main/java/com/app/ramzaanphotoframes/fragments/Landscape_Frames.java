package com.app.ramzaanphotoframes.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ramzaanphotoframes.R;

import java.util.ArrayList;

public class Landscape_Frames extends Fragment{

    private ArrayList<String> landscape_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            landscape_list = getArguments().getStringArrayList("Landscape");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landscape__frames, container, false);
    }
}
