package aiyuan1996.cn.firerunning;

import android.app.Application;

import aiyuan1996.cn.firerunning.Utils.ConfigUtils;
import aiyuan1996.cn.firerunning.Utils.ConstantUtils;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by aiyuan on 2017/2/16.
 */

public class MyApplication extends Application{
    public static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        // 读取配置文件
        ConfigUtils.getInstance().readConfig();
        // 依赖注入框架ButterKnife
        ButterKnife.setDebug(butterknife.BuildConfig.DEBUG);

        // 初始化Bmob
        if (!ConstantUtils.BMOB_APP_ID.isEmpty()) {
            BmobConfig config =new BmobConfig.Builder(this)
                    .setApplicationId(ConstantUtils.BMOB_APP_ID)// 设置appkey
                    .setConnectTimeout(30)// 请求超时时间（单位为秒）：默认15s
                    .setUploadBlockSize(1024*1024)// 文件分片上传时每片的大小（单位字节），默认512*1024
                    .setFileExpiration(2500)// 文件的过期时间(单位为秒)：默认1800s
                    .build();
            Bmob.initialize(config);
        }
    }
}
