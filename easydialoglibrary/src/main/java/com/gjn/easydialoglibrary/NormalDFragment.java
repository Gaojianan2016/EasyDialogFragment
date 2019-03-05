package com.gjn.easydialoglibrary;

import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gjn.easydialoglibrary.base.BaseDFragment;
import com.gjn.easydialoglibrary.base.IDFragmentConvertView;
import com.gjn.easydialoglibrary.base.ViewHolder;

/**
 * @author gjn
 * @time 2019/3/1 10:56
 */

public class NormalDFragment extends BaseDFragment {

    private AlertDialog.Builder dialogBuilder;
    @LayoutRes
    private int layoutId = View.NO_ID;

    private IDFragmentConvertView create;

    public static NormalDFragment newInstance(@LayoutRes int id) {
        return newInstance(id, null);
    }

    public static NormalDFragment newInstance(@LayoutRes int id, IDFragmentConvertView create) {
        NormalDFragment dFragment = new NormalDFragment();
        dFragment.layoutId = id;
        dFragment.create = create;
        return dFragment;
    }

    public static NormalDFragment newInstance() {
        return newInstance(null);
    }

    public static NormalDFragment newInstance(AlertDialog.Builder builder) {
        NormalDFragment dFragment = new NormalDFragment();
        dFragment.dialogBuilder = builder;
        return dFragment;
    }

    @Override
    protected AlertDialog.Builder getDialogBuilder() {
        return dialogBuilder;
    }

    @Override
    protected int getLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, DialogFragment dialogFragment) {
        if (create != null) {
            create.convertView(holder, dialogFragment);
        }
    }
}
