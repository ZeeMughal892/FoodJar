package com.zeeshan.foodjar.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerHelperTouchItemListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
