package com.czy.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:23
 * 说明：
 */
public class LetterIndexView extends View {

    private Paint paint;

    private boolean hit;

    private final String[] letters = {"↑", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};

    private final int DEFAULT_WIDTH;

    private Map<String, Integer> letterMap;

    private TextView tv_hint;

    private LinearLayoutManager linearLayoutManager;

    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        //字母颜色
        paint.setColor(Color.parseColor("#f94b4a4a"));
        DEFAULT_WIDTH = dpToPx(context, 24);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getWidthSize(widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    private int getWidthSize(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST: {
                if (widthSize >= DEFAULT_WIDTH) {
                    return DEFAULT_WIDTH;
                } else {
                    return widthSize;
                }
            }
            case MeasureSpec.EXACTLY: {
                return widthSize;
            }
            case MeasureSpec.UNSPECIFIED:
            default:
                return DEFAULT_WIDTH;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hit = true;
            case MotionEvent.ACTION_MOVE:
                onHit(event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                hit = false;
                hideHintTextView();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (hit) {
            //触摸字母索引条时的背景色
            canvas.drawColor(Color.parseColor("#9c646060"));
        } else {
            //没有触摸字母索引条时的背景色
            canvas.drawColor(Color.parseColor("#00ffffff"));
        }
        float letterHeight = ((float) getHeight()) / letters.length;
        float width = getWidth();
        float textSize = letterHeight * 5 / 7;
        paint.setTextSize(textSize);
        for (int i = 0; i < letters.length; i++) {
            canvas.drawText(letters[i], width / 2, letterHeight * i + textSize, paint);
        }
    }

    private void onHit(float offset) {
        if (tv_hint != null && linearLayoutManager != null && letterMap != null) {
            int index = (int) (offset / getHeight() * letters.length);
            index = Math.max(index, 0);
            index = Math.min(index, letters.length - 1);
            tv_hint.setText(letters[index]);
            tv_hint.setVisibility(View.VISIBLE);
            scrollToPosition(letters[index]);
        }
    }

    private void hideHintTextView() {
        if (tv_hint != null) {
            tv_hint.setVisibility(View.INVISIBLE);
        }
    }

    private void scrollToPosition(String letter) {
        int index = -1;
        if ("↑".equals(letter)) {
            index = 0;
        } else if (letterMap.containsKey(letter)) {
            index = letterMap.get(letter);
        }
        if (index < 0) {
            return;
        }
        linearLayoutManager.scrollToPositionWithOffset(index, 0);
    }

    public void bindIndexView(TextView tv_hint, LinearLayoutManager linearLayoutManager, Map<String, Integer> letterMap) {
        this.tv_hint = tv_hint;
        this.linearLayoutManager = linearLayoutManager;
        this.letterMap = letterMap;
    }

    private int dpToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}