package com.czy.ui.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:17
 * 说明：
 */
public class ListPickerDialog extends DialogFragment {

    //标题
    private String title;

    //选项
    private String[] options;

    private DialogInterface.OnClickListener listener;

    public void show(String title, String[] options, FragmentManager fragmentManager, DialogInterface.OnClickListener listener) {
        this.title = title;
        this.options = options;
        this.listener = listener;
        show(fragmentManager, "ListPickerDialog");
    }

    public void show(@StringRes int titleResource, String[] options, FragmentManager fragmentManager, DialogInterface.OnClickListener listener) {
        title = getString(titleResource);
        this.show(title, options, fragmentManager, listener);
    }

    public void show(String[] options, FragmentManager fragmentManager, DialogInterface.OnClickListener listener) {
        this.show(null, options, fragmentManager, listener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setItems(options, listener);
        return builder.create();
    }

}
