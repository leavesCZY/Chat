package com.czy.ui.recycler.wrap;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:16
 * 说明：可添加头部View与底部View的RecyclerView Adapter
 */
public class WrapRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter;

    private SparseArray<View> headerViewArray;

    private SparseArray<View> footerViewArray;

    //头部View类型开始位置,用于viewType
    private static int BASE_VIEW_TYPE_HEADER = 1000;

    //底部View类型开始位置,用于viewType
    private static int BASE_VIEW_TYPE_FOOTER = 2000;

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewsCountCount = getHeaderViewCount();
            notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount);
        }
    };

    public WrapRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        headerViewArray = new SparseArray<>();
        footerViewArray = new SparseArray<>();
        setAdapter(innerAdapter);
    }

    private void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        innerAdapter = adapter;
        innerAdapter.registerAdapterDataObserver(dataObserver);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return headerViewArray.keyAt(position);
        }
        if (isFooterPosition(position)) {
            return footerViewArray.keyAt(position - headerViewArray.size() - getDataItemCount());
        }
        return innerAdapter.getItemViewType(position - headerViewArray.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderView(viewType)) {
            return new ViewHolder(headerViewArray.get(viewType));
        }
        if (isFooterView(viewType)) {
            return new ViewHolder(footerViewArray.get(viewType));
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerViewsCountCount = getHeaderViewCount();
        if (position >= headerViewsCountCount && position < headerViewsCountCount + innerAdapter.getItemCount()) {
            innerAdapter.onBindViewHolder(holder, position - headerViewsCountCount);
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderViewCount() + getDataItemCount() + getFooterViewCount();
    }

    private int getDataItemCount() {
        return innerAdapter == null ? 0 : innerAdapter.getItemCount();
    }

    public int getHeaderViewCount() {
        return headerViewArray.size();
    }

    public int getFooterViewCount() {
        return footerViewArray.size();
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return innerAdapter;
    }

    /**
     * 判断是否是头部View
     *
     * @param viewType ViewType
     * @return 是否是头部View
     */
    private boolean isHeaderView(int viewType) {
        return headerViewArray.indexOfKey(viewType) > -1;
    }

    /**
     * 判断是否是底部View
     *
     * @param viewType ViewType
     * @return 是否是底部View
     */
    private boolean isFooterView(int viewType) {
        return footerViewArray.indexOfKey(viewType) > -1;
    }

    /**
     * 判断是否是头部View
     *
     * @param view View
     * @return 是否是头部View
     */
    public boolean isHeaderView(View view) {
        return headerViewArray.indexOfValue(view) > -1;
    }

    /**
     * 判断是否是底部View
     *
     * @param view View
     * @return 是否是底部View
     */
    public boolean isFooterView(View view) {
        return footerViewArray.indexOfValue(view) > -1;
    }

    /**
     * 根据索引判断该位置的View是否是头部View
     *
     * @param position 索引
     * @return 是否是头部View
     */
    private boolean isHeaderPosition(int position) {
        return position >= 0 && position < getHeaderViewCount();
    }

    /**
     * 根据索引判断该位置的View是否是底部View
     *
     * @param position 索引
     * @return 是否是底部View
     */
    private boolean isFooterPosition(int position) {
        return position >= (getHeaderViewCount() + getDataItemCount())
                && position < (getHeaderViewCount() + getDataItemCount() + getFooterViewCount());
    }

    /**
     * 添加头部View
     *
     * @param view 头部View
     */
    public void addHeaderView(View view) {
        if (headerViewArray.indexOfValue(view) < 0) {
            headerViewArray.put(BASE_VIEW_TYPE_HEADER++, view);
            notifyItemInserted(headerViewArray.size() - 1);
        }
    }

    /**
     * 添加底部View
     *
     * @param view 底部View
     */
    public void addFooterView(View view) {
        if (footerViewArray.indexOfValue(view) < 0) {
            footerViewArray.put(BASE_VIEW_TYPE_FOOTER++, view);
            notifyItemInserted(getHeaderViewCount() + getDataItemCount() + getFooterViewCount() - 1);
        }
    }

    /**
     * 移除头部View
     *
     * @param view View
     */
    public void removeHeaderView(View view) {
        int index = headerViewArray.indexOfValue(view);
        if (index > -1) {
            headerViewArray.removeAt(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * 移除底部View
     *
     * @param view View
     */
    public void removeFooterView(View view) {
        int index = footerViewArray.indexOfValue(view);
        if (index > -1) {
            footerViewArray.removeAt(index);
            notifyItemRemoved(getHeaderViewCount() + getDataItemCount() + index);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
