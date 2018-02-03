package com.czy.ui.recycler.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:14
 * 说明：通用RecyclerView Adapter
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private LayoutInflater layoutInflater;

    protected List<T> dataList;

    protected List<T> newDataList;

    private int layoutId;

    private static final int DEFAULT_ITEM_VIEW_TYPE = 10;

//    private static final int DIFF_UTIL_UPDATE = 20;
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == DIFF_UTIL_UPDATE) {
//                DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) msg.obj;
//                diffResult.dispatchUpdatesTo(CommonRecyclerViewAdapter.this);
//                CommonRecyclerViewAdapter.this.dataList = new ArrayList<>(newDataList);
//            }
//        }
//    };

    private DiffUtil.Callback callback = new DiffUtil.Callback() {

        @Override
        public int getOldListSize() {
            return getItemCount();
        }

        @Override
        public int getNewListSize() {
            return newDataList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return CommonRecyclerViewAdapter.this.areItemsTheSame(oldItemPosition, newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return CommonRecyclerViewAdapter.this.areContentsTheSame(oldItemPosition, newItemPosition);
        }

        @NonNull
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return CommonRecyclerViewAdapter.this.getChangePayload(oldItemPosition, newItemPosition);
        }
    };

    protected interface MultiTypeSupport<T> {
        int getLayoutId(T item, int position);
    }

    private MultiTypeSupport<T> multiTypeSupport;

    private CommonRecyclerViewHolder.OnClickListener clickListener;

    private CommonRecyclerViewHolder.OnLongClickListener longClickListener;

    private CommonRecyclerViewAdapter(Context context, List<T> dataList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.dataList = clone(dataList);
        this.newDataList = new ArrayList<>();
    }

    protected CommonRecyclerViewAdapter(Context context, List<T> dataList, int layoutId) {
        this(context, dataList);
        this.layoutId = layoutId;
    }

    protected CommonRecyclerViewAdapter(Context context, List<T> dataList, MultiTypeSupport<T> multiTypeSupport) {
        this(context, dataList);
        this.multiTypeSupport = multiTypeSupport;
    }

    public synchronized void setData(final List<T> dataList) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                handler.removeMessages(DIFF_UTIL_UPDATE);
//                newDataList.clear();
//                newDataList = CommonRecyclerViewAdapter.this.clone(dataList);
//                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, true);
//                Message message = new Message();
//                message.what = DIFF_UTIL_UPDATE;
//                message.obj = diffResult;
//                handler.sendMessage(message);
//            }
//        }).start();
        newDataList.clear();
        newDataList = clone(dataList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, true);
        diffResult.dispatchUpdatesTo(this);
        this.dataList = new ArrayList<>(newDataList);
    }

    @Override
    public int getItemViewType(int position) {
        if (multiTypeSupport != null) {
            return multiTypeSupport.getLayoutId(dataList.get(position), position);
        }
        return DEFAULT_ITEM_VIEW_TYPE;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (multiTypeSupport != null) {
            layoutId = viewType;
        }
        return new CommonRecyclerViewHolder(layoutInflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            partialBindData(holder, bundle);
        }
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        entirelyBindData(holder, dataList.get(position), position);
        if (clickListener != null) {
            holder.setClickListener(clickListener);
        }
        if (longClickListener != null) {
            holder.setLongClickListener(longClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setClickListener(CommonRecyclerViewHolder.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(CommonRecyclerViewHolder.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    private List<T> clone(List<T> dataList) {
        List<T> tempDataList = new ArrayList<>(dataList.size());
        for (T data : dataList) {
            tempDataList.add(clone(data));
        }
        return tempDataList;
    }

    /**
     * clone 指定对象，以此获得对象副本
     *
     * @param data 要复制的对象
     * @return 对象副本
     */
    protected abstract T clone(T data);

    /**
     * 判断数据列表刷新前后指定索引的位置是否指向同一条数据
     * 此处只对比两者是否指向同一条数据，而不关心其数据内容是否有变化
     *
     * @param oldItemPosition 更新前的数据索引
     * @param newItemPosition 更新后的数据索引
     * @return 是否指向同一条数据
     */
    protected abstract boolean areItemsTheSame(int oldItemPosition, int newItemPosition);

    /**
     * 此处来判断指向同一条数据的前后两个索引位置，其数据内容是否相同
     * 只在 areItemsTheSame 返回 true 时才会调用本方法
     *
     * @param oldItemPosition 更新前的数据索引
     * @param newItemPosition 更新后的数据索引
     * @return 数据内容是否有变化
     */
    protected abstract boolean areContentsTheSame(int oldItemPosition, int newItemPosition);

    /**
     * 获取同条数据在刷新前后是哪些数据内容发生了变化
     * 只在 areContentsTheSame 返回 false 时才会调用本方法
     *
     * @param oldItemPosition 更新前的数据索引
     * @param newItemPosition 更新后的数据索引
     * @return 数据变化内容
     */
    @NonNull
    protected abstract Bundle getChangePayload(int oldItemPosition, int newItemPosition);

    /**
     * 对刷新前后的数据进行定向更新，即只更新数据发生了变化的View
     *
     * @param holder Holder
     * @param bundle getChangePayload 方法的返回值
     */
    protected abstract void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle);

    /**
     * 对数据进行完全绑定
     *
     * @param holder Holder
     * @param data   Data
     */
    protected abstract void entirelyBindData(CommonRecyclerViewHolder holder, T data, int position);

}