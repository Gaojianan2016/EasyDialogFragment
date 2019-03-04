package com.shoumi.easydialogfragmentlibrary.base;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author gjn
 * @time 2019/3/1 10:46
 */

public class ViewHolder {
    private View view;
    private SparseArray<View> views;

    public ViewHolder(View view) {
        this.view = view;
        views = new SparseArray<>();
    }

    public static ViewHolder create(View view) {
        return new ViewHolder(view);
    }

    public static ViewHolder create(Activity activity, int id, ViewGroup container) {
        return create(LayoutInflater.from(activity).inflate(id, container, false));
    }

    public View getView() {
        return view;
    }

    public <T extends View> T findView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = this.view.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public void setTextViewText(int id, CharSequence text){
        getTextView(id).setText(text);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public ImageView getImageView(int id){
        return findView(id);
    }

    public void setViewOnClickListener(View.OnClickListener l){
        view.setOnClickListener(l);
    }

    public void setViewOnLongClickListener(View.OnLongClickListener l){
        view.setOnLongClickListener(l);
    }
}
