package com.gjn.easydialogfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gjn.easydialoglibrary.EasyDFragmentManager;

/**
 * @author gjn
 * @time 2019/3/6 9:58
 */

public class TestFM extends Fragment {

    private EasyDFragmentManager manager;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        manager = new EasyDFragmentManager(this);

        Button btn = view.findViewById(R.id.button);
        Button btn2 = view.findViewById(R.id.button2);
        Button btn3 = view.findViewById(R.id.button3);
        name = getArguments().getString("name", "null");

        btn2.setText(name);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showLargeLoading();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEasyNormalDialog("我是"+name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "点击"+name, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showLargeLoading("加载...");
            }
        });

        return view;
    }
}
