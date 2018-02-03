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
 * 时间：2017/11/27 23:23
 * 说明：
 */
public class SingleChoiceDialog extends DialogFragment {

    private String title;

    private String[] options;

    private int select;

    private DialogInterface.OnClickListener onClickListener;

    public void show(String title, String[] options, int select, FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {
        this.title = title;
        this.options = options;
        this.select = select;
        this.onClickListener = onClickListener;
        show(fragmentManager, "SingleChoiceDialogFragment");
    }

    public void show(@StringRes int titleResource, String[] options, int select, FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {
        title = getString(titleResource);
        this.show(title, options, select, fragmentManager, onClickListener);
    }

    public void show(String[] options, int select, FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {
        this.show(null, options, select, fragmentManager, onClickListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setSingleChoiceItems(options, select, onClickListener);
        return builder.create();
    }

}