package com.gjn.easydialoglibrary.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjn
 * @time 2019/2/27 17:16
 */

public abstract class BaseDFragment extends DialogFragment implements IDFragmentConvertView {
    private static final String TAG = "BaseDFragment";

    public static final int WRAP_CONTENT = ViewPager.LayoutParams.WRAP_CONTENT;
    public static final int MATCH_PARENT = ViewPager.LayoutParams.MATCH_PARENT;

    public static final float DIMAMOUT = 0.7f;

    private boolean isCloseOnTouchOutside = true;
    private boolean isCanClose = true;
    private boolean isShowAnimations = false;
    private boolean isTransparent = false;
    private int windowAnimations = -1;
    private float dimAmout = DIMAMOUT;
    private int width = WRAP_CONTENT;
    private int height = WRAP_CONTENT;
    private int gravity = Gravity.CENTER;

    private DialogCancelListener mDialogCancelListener;
    private List<DialogCancelListener> mDialogCancelListeners;

    protected abstract AlertDialog.Builder getDialogBuilder();

    protected abstract int getLayoutId();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getDialogBuilder() != null) {
            return getDialogBuilder().create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialogBuilder() == null && getLayoutId() != View.NO_ID) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            ViewHolder holder = ViewHolder.create(getActivity(), getLayoutId(), container);
            convertView(holder, this);
            return holder.getView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
            init();
        } catch (Exception e) {
            Log.w(TAG, "onStart: ", e);
        }
    }

    private void init() {
        getDialog().setCanceledOnTouchOutside(isCloseOnTouchOutside);
        getDialog().setCancelable(isCanClose);
        Window window = getDialog().getWindow();
        if (window != null) {
            if (isTransparent) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            WindowManager.LayoutParams params = window.getAttributes();
            if (dimAmout != DIMAMOUT) {
                params.dimAmount = dimAmout;
            }
            if (windowAnimations != -1 && isShowAnimations) {
                params.windowAnimations = windowAnimations;
            }
            params.width = width;
            params.height = height;
            params.gravity = gravity;
            window.setAttributes(params);
        }
    }

    public void setCloseOnTouchOutside(boolean closeOnTouchOutside) {
        isCloseOnTouchOutside = closeOnTouchOutside;
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(isCloseOnTouchOutside);
        }
    }

    public void setCanClose(boolean cancelable) {
        isCanClose = cancelable;
        if (getDialog() != null) {
            getDialog().setCancelable(isCanClose);
        }
    }

    public void setShowAnimations(boolean showAnimations) {
        isShowAnimations = showAnimations;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public void setWindowAnimations(int windowAnimations) {
        this.windowAnimations = windowAnimations;
        if (this.windowAnimations != -1) {
            setShowAnimations(true);
        }
    }

    public void setDimAmout(float dimAmout) {
        this.dimAmout = dimAmout;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            Log.w(TAG, "show: ", e);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mDialogCancelListener != null) {
            mDialogCancelListener.onCancel(this);
        }
        if (mDialogCancelListeners != null) {
            for (DialogCancelListener listener : mDialogCancelListeners) {
                listener.onCancel(this);
            }
        }
    }

    public void setOnDialogCancelListener(DialogCancelListener listener) {
        mDialogCancelListener = listener;
    }


    public void addOnDialogCancelListener(DialogCancelListener listener) {
        if (mDialogCancelListeners == null) {
            mDialogCancelListeners = new ArrayList<>();
        }
        mDialogCancelListeners.add(listener);
    }

    public void clearOnDialogCancelListeners() {
        if (mDialogCancelListeners != null) {
            mDialogCancelListeners.clear();
        }
    }

    public interface DialogCancelListener {
        void onCancel(DialogFragment dialogFragment);
    }
}
