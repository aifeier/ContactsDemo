package com.cwf.demo.contactsdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by n-240 on 2016/5/19.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class BaseActivity extends AppCompatActivity {

    /*是否使用Toolbar*/
    private boolean useToobar = true;

    /*标题*/
    private TextView titleTv;

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        if (useToobar) {
            View toobarView = LayoutInflater.from(this).inflate(R.layout.layout_base, null);
            super.setContentView(R.layout.layout_base);
            if (view != null && Build.VERSION.SDK_INT >= 14) {
                view.setFitsSystemWindows(true);
                toobarView.setFitsSystemWindows(true);
            }
            ((FrameLayout) findViewById(R.id.content)).addView(view);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            titleTv = (TextView) findViewById(R.id.title_textview);
            toolbar.setNavigationIcon(R.mipmap.back);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            super.setContentView(view);
        }
    }


    public void setUseToobar(boolean useToobar) {
        this.useToobar = useToobar;
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        super.onBackPressed();
    }
}
