package com.app.ramzaanphotoframes.recycler_click_listener;

import android.view.View;

/**
 * Created by 2136 on 2/12/2018.
 */

public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
