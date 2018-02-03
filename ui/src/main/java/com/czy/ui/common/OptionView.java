package com.czy.ui.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czy.ui.R;
import com.czy.ui.dialog.ListPickerDialog;
import com.czy.ui.dialog.SingleChoiceDialog;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:25
 * 说明：
 */
public class OptionView extends RelativeLayout {

    private String title;

    private String content;

    private boolean showDivider = true;

    public OptionView(Context context) {
        this(context, null);
    }

    public OptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_option, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionView, 0, 0);
        title = typedArray.getString(R.styleable.OptionView_title);
        content = typedArray.getString(R.styleable.OptionView_content);
        showDivider = typedArray.getBoolean(R.styleable.OptionView_showDivider, true);
        typedArray.recycle();
        if (!TextUtils.isEmpty(title)) {
            TextView tv_optionTitle = (TextView) findViewById(R.id.tv_optionTitle);
            tv_optionTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            TextView tv_optionContent = (TextView) findViewById(R.id.tv_optionContent);
            tv_optionContent.setText(content);
        }
        if (!showDivider) {
            View view_optionDivider = findViewById(R.id.view_optionDivider);
            view_optionDivider.setVisibility(INVISIBLE);
        }
        setClickable(true);
        setSaveEnabled(true);
    }

    public void setTitle(String title) {
        if ((title != null && title.equals(this.title)) || (title == null && this.title == null)) {
            return;
        }
        this.title = title;
        TextView tv_optionTitle = (TextView) findViewById(R.id.tv_optionTitle);
        tv_optionTitle.setText(title);
    }

    public void setContent(String content) {
        if ((content != null && content.equals(this.content)) || (content == null && this.content == null)) {
            return;
        }
        this.content = content;
        TextView tv_optionContent = (TextView) findViewById(R.id.tv_optionContent);
        tv_optionContent.setText(content);
    }

    public String getContent() {
        return content;
    }

    public void setDividerVisibility(boolean showDivider) {
        if (showDivider ^ this.showDivider) {
            this.showDivider = showDivider;
            View view_optionDivider = findViewById(R.id.view_optionDivider);
            view_optionDivider.setVisibility(showDivider ? VISIBLE : INVISIBLE);
        }
    }

    public void setOnClickShowPickerDialog(final String title, final String[] options, final FragmentManager fragmentManager, final onClickOptionListener clickOptionListener) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListPickerDialog().show(title, options, fragmentManager, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickOptionListener != null) {
                            clickOptionListener.onClick(getId(), which, options[which]);
                        }
                    }
                });
            }
        });
    }

    public void setOnClickShowSingleChoiceDialog(final String title, final String[] options, final int select, final FragmentManager fragmentManager, final onClickOptionListener clickOptionListener) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleChoiceDialog().show(title, options, select, fragmentManager, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickOptionListener.onClick(getId(), which, options[which]);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public interface onClickOptionListener {
        void onClick(int id, int which, String content);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcelable", parcelable);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putBoolean("showDivider", showDivider);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        super.onRestoreInstanceState(bundle.getParcelable("parcelable"));
        setTitle(bundle.getString("title"));
        setContent(bundle.getString("content"));
        setDividerVisibility(bundle.getBoolean("showDivider"));
    }

}
