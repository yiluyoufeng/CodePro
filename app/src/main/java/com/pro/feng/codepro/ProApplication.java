package com.pro.feng.codepro;

import android.app.Application;

/**
 * Created by Feng on 2017/12/1.
 */

public class ProApplication extends Application{
    /**
     * 应用程序实例对象
     */
    private static ProApplication instance = null;

    /* 返回一个全局管理实例 */
    public static synchronized ProApplication getInstance() {

        if (instance == null) {
            instance = new ProApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
