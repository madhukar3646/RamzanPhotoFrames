package com.app.ramzaanphotoframes.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.AppsParkAdsAdapter;
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
        View view=inflater.inflate(R.layout.fragment_morefree_apps, container, false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        RecyclerView frames_recycle_view = (RecyclerView)view.findViewById(R.id.frames_recycle_view);
        frames_recycle_view.setHasFixedSize(true);
        frames_recycle_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        AppsParkAdsAdapter custom_adapter = new AppsParkAdsAdapter(getActivity(), apps_list);
        frames_recycle_view.setAdapter(custom_adapter);
    }

}
