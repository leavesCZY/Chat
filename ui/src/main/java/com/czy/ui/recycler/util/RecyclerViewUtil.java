package com.czy.ui.recycler.util;

import android.support.v7.widget.RecyclerView;

import com.czy.ui.recycler.wrap.WrapRecyclerViewAdapter;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:16
 * 说明：
 */
public class RecyclerViewUtil {

    public static int getRecyclerViewAdapterPosition(RecyclerView recyclerView, int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof WrapRecyclerViewAdapter) {
            return position - ((WrapRecyclerViewAdapter) adapter).getHeaderViewCount();
        }
        return position;
    }

}
