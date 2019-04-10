package com.gjn.easydialoglibrary;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gjn.easydialoglibrary.base.BaseDFragment;
import com.gjn.easydialoglibrary.base.IDFragmentConvertView;
import com.gjn.easydialoglibrary.base.ViewHolder;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author gjn
 * @time 2019/3/1 16:00
 */

public class EasyDFragmentManager {
    private static final String TAG = "EasyDFragmentManager";

    public final static int SMALL_SIZE = 2;
    public final static int MIDDLE_SIZE = 1;
    public final static int LARGE_SIZE = 0;

    private FragmentManager fm;
    private Activity activity;

    private CopyOnWriteArrayList<BaseDFragment> mDFragments;
    private BaseDFragment.DialogCancelListener dialogCancelListener;

    private NormalDFragment smallLoadingDialog;
    private NormalDFragment middleLoadingDialog;
    private NormalDFragment largeLoadingDialog;

    public EasyDFragmentManager(AppCompatActivity activity) {
        fm = activity.getSupportFragmentManager();
        this.activity = activity;
        init();
    }

    public EasyDFragmentManager(Fragment fragment) {
        fm = fragment.getChildFragmentManager();
        activity = fragment.getActivity();
        init();
    }

    private void init() {
        mDFragments = new CopyOnWriteArrayList<>();
        dialogCancelListener = new BaseDFragment.DialogCancelListener() {
            @Override
            public void onCancel(DialogFragment dialogFragment) {
                Log.i(TAG, "touch or back dissmiss: " + dialogFragment);
                mDFragments.remove(dialogFragment);
            }
        };
        smallLoadingDialog = getLoadingDialog(SMALL_SIZE);
        middleLoadingDialog = getLoadingDialog(MIDDLE_SIZE);
        largeLoadingDialog = getLoadingDialog(LARGE_SIZE);
    }

    private void show(BaseDFragment dFragment) {
        Log.i(TAG, "show: " + dFragment);
        mDFragments.add(dFragment);
        dFragment.setOnDialogCancelListener(dialogCancelListener);
        dFragment.show(fm, dFragment.getTag());
    }

    private void dissmiss(BaseDFragment dFragment) {
        Log.i(TAG, "dissmiss: " + dFragment);
        mDFragments.remove(dFragment);
        dFragment.clearOnDialogCancelListeners();
        dFragment.dismissAllowingStateLoss();
    }

    public void showDialog(BaseDFragment dFragment) {
        if (dFragment == null) {
            Log.w(TAG, "dFragment is null.");
            return;
        }
        dismissDialog(dFragment);
        show(dFragment);
    }

    public void showOnlyDialog(BaseDFragment dFragment) {
        if (dFragment == null) {
            Log.w(TAG, "dFragment is null.");
            return;
        }
        if (!mDFragments.contains(dFragment)) {
            show(dFragment);
        }
    }

    public void dismissDialog(BaseDFragment dFragment) {
        if (mDFragments.contains(dFragment)) {
            dissmiss(dFragment);
        }
    }

    public void clearDialog() {
        for (BaseDFragment dFragment : mDFragments) {
            dismissDialog(dFragment);
        }
        mDFragments.clear();
    }

    public void addOnDialogCancelListener(BaseDFragment.DialogCancelListener listener) {
        for (BaseDFragment dFragment : mDFragments) {
            dFragment.addOnDialogCancelListener(listener);
        }
    }

    public NormalDFragment getEasyDialog(int id, IDFragmentConvertView convertView) {
        NormalDFragment dFragment = NormalDFragment.newInstance(id, convertView);
        dFragment.setTransparent(true);
        return dFragment;
    }

    public NormalDFragment showNormalDFragment(int id) {
        return showNormalDFragment(id, false, BaseDFragment.DIMAMOUT);
    }

    public NormalDFragment showNormalDFragment(int id, float dimAmout) {
        return showNormalDFragment(id, false, dimAmout);
    }

    public NormalDFragment showNormalDFragment(int id, boolean isTransparent, float dimAmout) {
        NormalDFragment dFragment = NormalDFragment.newInstance(id);
        dFragment.setTransparent(isTransparent);
        dFragment.setDimAmout(dimAmout);
        showDialog(dFragment);
        return dFragment;
    }

    public NormalDFragment showNormalDFragment(CharSequence title, CharSequence msg, DialogInterface.OnClickListener yesOnClickListener) {
        return showNormalDFragment(title, msg, "是", yesOnClickListener, "否", null);
    }

    public NormalDFragment showNormalDFragment(CharSequence title, CharSequence msg,
                                               CharSequence yes, DialogInterface.OnClickListener yesOnClickListener,
                                               CharSequence no, DialogInterface.OnClickListener noOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(msg).setPositiveButton(yes, yesOnClickListener)
                .setNegativeButton(no, noOnClickListener);
        NormalDFragment dFragment = NormalDFragment.newInstance(builder);
        showDialog(dFragment);
        return dFragment;
    }

    public NormalDFragment showEasyNormalDialog(CharSequence msg, View.OnClickListener yesOnClickListener) {
        return showEasyNormalDialog(msg, yesOnClickListener, null);
    }

    public NormalDFragment showEasyNormalDialog(CharSequence msg, View.OnClickListener yesOnClickListener, View.OnClickListener noOnClickListener) {
        return showEasyNormalDialog(BaseDFragment.DIMAMOUT, false, msg, "确定", yesOnClickListener, "取消", noOnClickListener);
    }

    public NormalDFragment showEasyOneBtnDialog(final CharSequence msg, final CharSequence yes,
                                                final View.OnClickListener yesOnClickListener) {
        return showEasyNormalDialog(BaseDFragment.DIMAMOUT, true, msg, yes, yesOnClickListener, "", null);
    }

    public NormalDFragment showEasyInputDialog(final CharSequence msg, final int maxSize,
                                               final EasyInputListener inputListener) {
        return showEasyInputDialog(BaseDFragment.DIMAMOUT, msg, "提交", maxSize, inputListener);
    }

    public NormalDFragment showEasyDelayDialog(CharSequence msg, int time, View.OnClickListener yesOnClickListener) {
        return showEasyDelayDialog(BaseDFragment.DIMAMOUT, msg, time, "确定", yesOnClickListener);
    }

    public NormalDFragment showEasyNormalDialog(float dimAmout, final boolean isOneBtn, final CharSequence msg, final CharSequence yes,
                                                final View.OnClickListener yesOnClickListener, final CharSequence no,
                                                final View.OnClickListener noOnClickListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_easy_normal, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_easydialog, msg);
                holder.setTextViewText(R.id.yes_easydialog, yes);
                holder.findView(R.id.yes_easydialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yesOnClickListener != null) {
                            yesOnClickListener.onClick(v);
                        }
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
                if (isOneBtn) {
                    holder.findView(R.id.line_easydialog).setVisibility(View.GONE);
                    holder.findView(R.id.no_easydialog).setVisibility(View.GONE);
                } else {
                    holder.setTextViewText(R.id.no_easydialog, no);
                    holder.findView(R.id.no_easydialog).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (noOnClickListener != null) {
                                noOnClickListener.onClick(v);
                            }
                            dialogFragment.dismissAllowingStateLoss();
                        }
                    });
                }
            }
        });
        dFragment.setDimAmout(dimAmout);
        showDialog(dFragment);
        return dFragment;
    }

    public NormalDFragment showEasyInputDialog(float dimAmout, final CharSequence msg, final CharSequence yes, final int maxSize,
                                               final EasyInputListener inputListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_easy_input, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_easydialog, msg);
                holder.setTextViewText(R.id.yes_easydialog, yes);
                final TextView tv = holder.findView(R.id.size_easydialog);
                tv.setText(String.valueOf(maxSize));
                tv.setTextColor(Color.BLACK);
                final EditText et = holder.findView(R.id.content_easydialog);
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > maxSize) {
                            tv.setText("超出数量 " + (s.length() - maxSize));
                            tv.setTextColor(Color.RED);
                        } else {
                            tv.setText(String.valueOf(maxSize - s.length()));
                            tv.setTextColor(Color.BLACK);
                        }
                    }
                });
                holder.findView(R.id.yes_easydialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inputListener != null) {
                            inputListener.confirm(v, et.getText(), maxSize);
                        }
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
                holder.findView(R.id.no_easydialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
            }
        });
        dFragment.setDimAmout(dimAmout).setCloseOnTouchOutside(false);
        showDialog(dFragment);
        return dFragment;
    }

    public NormalDFragment showEasyDelayDialog(float dimAmout, final CharSequence msg, final int time, final CharSequence yes,
                                               final View.OnClickListener yesOnClickListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_easy_normal, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_easydialog, msg);
                final TextView tvYes = holder.findView(R.id.yes_easydialog);
                tvYes.setText(yes + "(" + time + ")");
                tvYes.setTextColor(Color.GRAY);
                tvYes.setEnabled(false);
                final CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvYes.setText(yes + "(" + (millisUntilFinished / 1000) + ")");
                    }

                    @Override
                    public void onFinish() {
                        tvYes.setText(yes);
                        tvYes.setEnabled(true);
                        tvYes.setTextColor(Color.BLACK);
                    }
                };
                timer.start();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yesOnClickListener != null) {
                            yesOnClickListener.onClick(v);
                        }
                        timer.cancel();
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
                holder.findView(R.id.no_easydialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
            }
        });
        dFragment.setDimAmout(dimAmout).setCloseOnTouchOutside(false);
        showDialog(dFragment);
        return dFragment;
    }

    public NormalDFragment getLoadingDialog(final int size) {
        return getLoadingDialog(size, null);
    }

    public NormalDFragment getLoadingDialog(final int size, final CharSequence loadtext) {
        int edge = activity.getResources().getDisplayMetrics().widthPixels;
        switch (size) {
            case SMALL_SIZE:
                edge /= 7;
                break;
            case MIDDLE_SIZE:
                edge /= 4;
                break;
            case LARGE_SIZE:
            default:
                edge /= 3;
                break;
        }
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_easy_loading, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, DialogFragment dialogFragment) {
                TextView tv = holder.findView(R.id.loadtext_easydialog);
                ProgressBar pb = holder.findView(R.id.loading_easydialog);
                int padding;
                if (size == SMALL_SIZE) {
                    tv.setVisibility(View.GONE);
                    padding = px2Dip(5);
                }else {
                    if (loadtext == null) {
                        tv.setVisibility(View.GONE);
                        if (size == MIDDLE_SIZE) {
                            padding = px2Dip(20);
                        }else {
                            padding = px2Dip(35);
                        }
                    }else {
                        tv.setText(loadtext);
                        if (size == MIDDLE_SIZE) {
                            padding = px2Dip(5);
                        }else {
                            padding = px2Dip(10);
                        }
                    }
                    if (size == MIDDLE_SIZE) {
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    }else {
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    }
                }
                pb.setPadding(padding, padding, padding, padding);
            }
        });
        dFragment.setWidth(edge).setHeight(edge).setCloseOnTouchOutside(false);
        return dFragment;
    }

    private int px2Dip(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size,
                activity.getResources().getDisplayMetrics());
    }

    public NormalDFragment showLargeLoading() {
        largeLoadingDialog.setCreate(getLoadingDialog(LARGE_SIZE));
        showOnlyDialog(largeLoadingDialog);
        return largeLoadingDialog;
    }

    public NormalDFragment showMiddleLoading() {
        middleLoadingDialog.setCreate(getLoadingDialog(MIDDLE_SIZE));
        showOnlyDialog(middleLoadingDialog);
        return middleLoadingDialog;
    }

    public NormalDFragment showSmallLoading() {
        smallLoadingDialog = getLoadingDialog(SMALL_SIZE);
        showOnlyDialog(smallLoadingDialog);
        return smallLoadingDialog;
    }

    public NormalDFragment showLargeLoading(CharSequence loadtext) {
        largeLoadingDialog.setCreate(getLoadingDialog(LARGE_SIZE, loadtext));
        showOnlyDialog(largeLoadingDialog);
        return largeLoadingDialog;
    }

    public NormalDFragment showMiddleLoading(CharSequence loadtext) {
        middleLoadingDialog.setCreate(getLoadingDialog(MIDDLE_SIZE, loadtext));
        showOnlyDialog(middleLoadingDialog);
        return middleLoadingDialog;
    }

    public NormalDFragment getSmallLoadingDialog() {
        return smallLoadingDialog;
    }

    public NormalDFragment getMiddleLoadingDialog() {
        return middleLoadingDialog;
    }

    public NormalDFragment getLargeLoadingDialog() {
        return largeLoadingDialog;
    }

    public NormalDFragment showLoading(float dimAmout, final int size, final CharSequence loadtext) {
        NormalDFragment dFragment = getLoadingDialog(size, loadtext);
        dFragment.setDimAmout(dimAmout);
        showDialog(dFragment);
        return dFragment;
    }

    public interface EasyInputListener {
        void confirm(View v, Editable msg, int maxSize);
    }
}
