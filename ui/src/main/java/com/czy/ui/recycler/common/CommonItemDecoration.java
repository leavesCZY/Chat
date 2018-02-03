package com.czy.ui.recycler.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.czy.ui.utils.ScreenUtils;

/**
 * 作者：叶应是叶
 * 时间：2017/12/21 21:52
 * 说明：通用分隔线
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {

    private int orientation = LinearLayoutManager.HORIZONTAL;

    private Drawable drawable;

    private int leftMargin;

    private int rightMargin;

    public CommonItemDecoration(Context context, int orientation) {
        this.orientation = orientation;
        int[] attrs = new int[]{android.R.attr.listDivider};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        drawable = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    public CommonItemDecoration(Drawable drawable, int orientation) {
        this.drawable = drawable;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            drawVerticalDivider(c, parent);
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            drawHorizontalDivider(c, parent);
        }
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int left = child.getRight();
            int top = child.getTop();
            int right = left + drawable.getIntrinsicWidth();
            int bottom = child.getBottom();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int left;
        int right;
        int top;
        int bottom;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        View child;
        RecyclerView.LayoutParams params;
        for (int i = 0; i < parent.getChildCount(); i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin;
            bottom = top + drawable.getIntrinsicHeight();
            drawable.setBounds(left + ScreenUtils.dp2px(parent.getContext(), leftMargin), top, right - ScreenUtils.dp2px(parent.getContext(), rightMargin), bottom);
            drawable.draw(c);
        }
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

}
