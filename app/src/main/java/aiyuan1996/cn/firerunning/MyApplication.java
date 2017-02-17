package aiyuan1996.cn.firerunning;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by aiyuan on 2017/2/16.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
    }
}
