package com.app.ramzaanphotoframes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.activities.Frames_Activity;
import com.app.ramzaanphotoframes.adapters.Frames_Adapter;

import java.util.ArrayList;

public class Portrait_Frames extends Fragment{

    private ArrayList<String> portrait_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portrait_list = getArguments().getStringArrayList("Portrait");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_portrait__frames, container, false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        RecyclerView frames_recycle_view = (RecyclerView)view.findViewById(R.id.frames_recycle_view);
        frames_recycle_view.setHasFixedSize(true);
        frames_recycle_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //Frames_Adapter custom_adapter = new Frames_Adapter(getActivity(), frame_items);
        //frames_recycle_view.setAdapter(custom_adapter);
    }
}
