package com.snappwish.smarthotel.base;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.snappwish.smarthotel.BuildConfig;

/**
 * Created by jinjin on 17/11/6.
 * description:
 */

public class MyBaseApplication extends Application{

    public static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppContext = this;

        initConfig();

        initHttpConfig();

        // 调试数据库和网络 http://www.jianshu.com/p/03da9f91f41f
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

    }

    public Context getContext() {
        return sAppContext;
    }

    private void initHttpConfig() {

    }

    private void initConfig() {
        // 初始化icons
    }
}
