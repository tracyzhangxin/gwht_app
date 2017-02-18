package com.example.administrator.gwht;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/2/15.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 开启debug会影响性能
    }
}