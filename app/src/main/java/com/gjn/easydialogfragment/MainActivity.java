package com.gjn.easydialogfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.shoumi.easydialogfragmentlibrary.DFragmentManager;

public class MainActivity extends AppCompatActivity {

    private DFragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new DFragmentManager(this);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showNormalDFragment("提示", "我是一个默认对话框", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "点击是", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEasyNormalDialog("我是Easy自带的普通样式", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击是", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEasyOneBtnDialog("我是Easy自带的单按钮样式", "我是单按钮", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击单按钮", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEasyInputDialog("我是Easy自带的输入样式", 50, new DFragmentManager.EasyInputListener() {
                    @Override
                    public void confirm(View v, Editable msg, int maxSize) {
                        Toast.makeText(MainActivity.this, "你输入了\n" + msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEasyDelayDialog("我是Easy自带的延时样式", 5, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "延时点击", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showLargeLoading();
            }
        });

        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showMiddleLoading();
            }
        });

        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showSmallLoading();
            }
        });

        findViewById(R.id.btn9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showNormalDFragment(R.layout.dialog_test);
            }
        });
    }

    @Override
    protected void onDestroy() {
        manager.clearDialog();
        super.onDestroy();
    }
}
