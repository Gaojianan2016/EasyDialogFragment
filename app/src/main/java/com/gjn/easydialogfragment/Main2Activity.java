package com.gjn.easydialogfragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gjn.bottombarlibrary.BarTab;
import com.gjn.bottombarlibrary.BottomBarV4View;
import com.gjn.bottombarlibrary.IBarTab;
import com.gjn.bottombarlibrary.OnBindBarDateListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private BottomBarV4View bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bar = findViewById(R.id.bbv_main2);

        List<IBarTab> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            BarTab barTab = new BarTab();
            barTab.setTitle("tab"+i);
            barTab.setCls(TestFM.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", "tab "+ i);
            barTab.setBundle(bundle);
            list.add(barTab);
        }

        bar.setOnBindBarDateListener(new OnBindBarDateListener() {
            @Override
            public void onBindBarView(View view, int i, IBarTab iBarTab) {
                TextView tv = view.findViewById(R.id.tv_bar);
                tv.setText(iBarTab.getTitle());
            }
        }).updataView(list);
    }

    @Override
    protected void onDestroy() {
        bar.destroy();
        super.onDestroy();
    }
}
