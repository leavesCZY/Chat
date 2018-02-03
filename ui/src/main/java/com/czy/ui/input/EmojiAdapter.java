package com.czy.ui.input;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.czy.ui.R;
import com.czy.ui.input.model.Emoji;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.czy.ui.utils.ScreenUtils;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 18:57
 * 说明：
 */
public class EmojiAdapter extends CommonRecyclerViewAdapter<Emoji> {

    private Context context;

    EmojiAdapter(Context context, List<Emoji> dataList) {
        super(context, dataList, R.layout.item_emoji);
        this.context = context;
    }

    @Override
    protected Emoji clone(Emoji data) {
        return new Emoji(data.getName(), data.getId());
    }

    @Override
    protected boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @NonNull
    @Override
    protected Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
        return null;
    }

    @Override
    protected void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle) {

    }

    @Override
    protected void entirelyBindData(CommonRecyclerViewHolder holder, Emoji data, int position) {
        final int spanCount = EmojiFragment.RECYCLER_VIEW_SPAN_COUNT;
        int screenWidth = ScreenUtils.getScreenWidth((Activity) context);
        int spacing = ScreenUtils.dp2px(context, 12);
        int temp = spacing * (spanCount + 1);
        int emoticonSideLength = (screenWidth - temp) / spanCount;
        holder.setImageResource(R.id.iv_emoticon, data.getId())
                .setViewPadding(R.id.iv_emoticon, temp / spanCount, temp / spanCount, 0, 0)
                .setImageViewLayoutParams(R.id.iv_emoticon, emoticonSideLength, emoticonSideLength);
    }

}
