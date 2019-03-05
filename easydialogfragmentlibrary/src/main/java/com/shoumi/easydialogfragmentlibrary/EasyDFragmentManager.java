package com.shoumi.easydialogfragmentlibrary;

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
import android.widget.TextView;

import com.shoumi.easydialogfragmentlibrary.base.BaseDFragment;
import com.shoumi.easydialogfragmentlibrary.base.IDFragmentConvertView;
import com.shoumi.easydialogfragmentlibrary.base.ViewHolder;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author gjn
 * @time 2019/3/1 16:00
 */

public class EasyDFragmentManager {
    public final static int SMALL_SIZE = 2;
    public final static int MIDDLE_SIZE = 1;
    public final static int LARGE_SIZE = 0;
    private static final String TAG = "EasyDFragmentManager";
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

    public void showNormalDFragment(int id) {
        showNormalDFragment(id, false, BaseDFragment.DIMAMOUT);
    }

    public void showNormalDFragment(int id, float dimAmout) {
        showNormalDFragment(id, false, dimAmout);
    }

    public void showNormalDFragment(int id, boolean isTransparent, float dimAmout) {
        NormalDFragment dFragment = NormalDFragment.newInstance(id);
        dFragment.setTransparent(isTransparent);
        dFragment.setDimAmout(dimAmout);
        showDialog(dFragment);
    }

    public void showNormalDFragment(CharSequence title, CharSequence msg, DialogInterface.OnClickListener yesOnClickListener) {
        showNormalDFragment(title, msg, "是", yesOnClickListener, "否", null);
    }

    public void showNormalDFragment(CharSequence title, CharSequence msg,
                                    CharSequence yes, DialogInterface.OnClickListener yesOnClickListener,
                                    CharSequence no, DialogInterface.OnClickListener noOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(msg).setPositiveButton(yes, yesOnClickListener)
                .setNegativeButton(no, noOnClickListener);
        showDialog(NormalDFragment.newInstance(builder));
    }

    public void showEasyNormalDialog(CharSequence msg, View.OnClickListener yesOnClickListener) {
        showEasyNormalDialog(msg, yesOnClickListener, null);
    }

    public void showEasyNormalDialog(CharSequence msg, View.OnClickListener yesOnClickListener, View.OnClickListener noOnClickListener) {
        showEasyNormalDialog(BaseDFragment.DIMAMOUT, false, msg, "确定", yesOnClickListener, "取消", noOnClickListener);
    }

    public void showEasyOneBtnDialog(final CharSequence msg, final CharSequence yes,
                                     final View.OnClickListener yesOnClickListener) {
        showEasyNormalDialog(BaseDFragment.DIMAMOUT, true, msg, yes, yesOnClickListener, "", null);
    }

    public void showEasyInputDialog(final CharSequence msg, final int maxSize,
                                    final EasyInputListener inputListener) {
        showEasyInputDialog(BaseDFragment.DIMAMOUT, msg, "提交", maxSize, inputListener);
    }

    public void showEasyDelayDialog(CharSequence msg, int time, View.OnClickListener yesOnClickListener) {
        showEasyDelayDialog(BaseDFragment.DIMAMOUT, msg, time, "确定", yesOnClickListener);
    }

    public void showEasyNormalDialog(float dimAmout, final boolean isOneBtn, final CharSequence msg, final CharSequence yes,
                                     final View.OnClickListener yesOnClickListener, final CharSequence no,
                                     final View.OnClickListener noOnClickListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_ios_normal, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_dialog, msg);
                holder.setTextViewText(R.id.yes_dialog, yes);
                holder.findView(R.id.yes_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yesOnClickListener != null) {
                            yesOnClickListener.onClick(v);
                        }
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
                if (isOneBtn) {
                    holder.findView(R.id.line_dialog).setVisibility(View.GONE);
                    holder.findView(R.id.no_dialog).setVisibility(View.GONE);
                } else {
                    holder.setTextViewText(R.id.no_dialog, no);
                    holder.findView(R.id.no_dialog).setOnClickListener(new View.OnClickListener() {
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
    }

    public void showEasyInputDialog(float dimAmout, final CharSequence msg, final CharSequence yes, final int maxSize,
                                    final EasyInputListener inputListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_ios_input, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_dialog, msg);
                holder.setTextViewText(R.id.yes_dialog, yes);
                final TextView tv = holder.findView(R.id.size_dialog);
                tv.setText(String.valueOf(maxSize));
                tv.setTextColor(Color.BLACK);
                final EditText et = holder.findView(R.id.content_dialog);
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
                holder.findView(R.id.yes_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inputListener != null) {
                            inputListener.confirm(v, et.getText(), maxSize);
                        }
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
                holder.findView(R.id.no_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
            }
        });
        dFragment.setDimAmout(dimAmout);
        dFragment.setCloseOnTouchOutside(false);
        showDialog(dFragment);
    }

    public void showEasyDelayDialog(float dimAmout, final CharSequence msg, final int time, final CharSequence yes,
                                    final View.OnClickListener yesOnClickListener) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_ios_normal, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, final DialogFragment dialogFragment) {
                holder.setTextViewText(R.id.msg_dialog, msg);
                final TextView tvYes = holder.findView(R.id.yes_dialog);
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
                holder.findView(R.id.no_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        dialogFragment.dismissAllowingStateLoss();
                    }
                });
            }
        });
        dFragment.setDimAmout(dimAmout);
        dFragment.setCloseOnTouchOutside(false);
        showDialog(dFragment);
    }

    public NormalDFragment getLoadingDialog(final int size) {
        return getLoadingDialog(size, null);
    }

    public NormalDFragment getLoadingDialog(final int size, final CharSequence loadtext) {
        NormalDFragment dFragment = getEasyDialog(R.layout.dialog_loading, new IDFragmentConvertView() {
            @Override
            public void convertView(ViewHolder holder, DialogFragment dialogFragment) {
                TextView tv = holder.findView(R.id.loadtext_dialog);
                if (loadtext != null) {
                    tv.setText(loadtext);
                } else {
                    tv.setVisibility(View.GONE);
                }
                switch (size) {
                    case SMALL_SIZE:
                        tv.setVisibility(View.GONE);
                        break;
                    case MIDDLE_SIZE:
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        break;
                    case LARGE_SIZE:
                    default:
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        break;
                }
            }
        });
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
        dFragment.setWidth(edge);
        dFragment.setHeight(edge);
        dFragment.setCloseOnTouchOutside(false);
        return dFragment;
    }

    public void showLargeLoading() {
        showDialog(largeLoadingDialog);
    }

    public void showMiddleLoading() {
        showDialog(middleLoadingDialog);
    }

    public void showSmallLoading() {
        showDialog(smallLoadingDialog);
    }

    public void showLoading(float dimAmout, final int size, final CharSequence loadtext) {
        NormalDFragment dFragment = getLoadingDialog(size, loadtext);
        dFragment.setDimAmout(dimAmout);
        showDialog(dFragment);
    }

    public interface EasyInputListener {
        void confirm(View v, Editable msg, int maxSize);
    }
}
