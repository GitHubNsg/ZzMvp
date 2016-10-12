package com.zzm.zzmlibrary.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;
import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmlibrary.utils.TDevice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * 全局上下文,初始化程序和第三方SDK的配置，抓取崩溃信息
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    public static long MainThreadId;
    private static Handler handler;
    public static int mNetWorkState;

    public static int titleBarBackgroundColor;
    public static int titleBarTextColor;


    @Override
    public void onCreate() {
        super.onCreate();
        Constants.init(this);
        mApplication = this;
        handler = new Handler();
        MainThreadId = android.os.Process.myTid();
        mNetWorkState = TDevice.getNetworkType();
        // 设置错误的管理器抓取异常信息
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        //配置资源文件
        initResource();
        //配置第三方SDK
        initService();
    }

    protected void initResource() {
        titleBarTextColor = Color.parseColor("#ffffff");
        titleBarBackgroundColor = Color.parseColor("#000000");

    }

    protected void initService() {
        initOkHttp();
        initShare();
        initPush();

    }

    /**
     * 配置阿里云推送,如果是小米或者华为手机则注册为系统级服务，否则跳过
     */
    protected void initPush() {
    }

    /**
     * 配置友盟分享平台
     */
    protected void initShare() {
    }

    /**
     * 配置OkHttpClient
     */
    protected void initOkHttp(){
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(mApplication));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggerInterceptor("OKHTTP_TAG"))
//                .addInterceptor(RequestHeaders.addHeaderInterceptor())
                .connectTimeout(6000L, TimeUnit.MILLISECONDS)
                .readTimeout(6000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 抓取错误
     */
    private class MyUncaughtExceptionHandler implements
            Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
			LogUtils.e("发现了错误:");
            ex.printStackTrace();
            dealWrongText(ex);
        }
    }

    /**
     * 处理捕获的错误
     * @param ex
     */
    protected void dealWrongText(Throwable ex) {
//        FileUtil.write(application,"app_wrong.txt",ex.toString());
        //程序闪退或anr重新打开app
            /*if(!LogUtils.APP_DBG && BaseActivity.activity != null
                    && !(BaseActivity.activity instanceof MainActivity)){
                Intent intent = new Intent(BaseApplication.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.this.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }*/
    }

    /**
     * 获取handler
     * @return
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取Application
     * @return
     */
    public static synchronized BaseApplication getInstance() {
        return mApplication;
    }

    /**
     * 防止方法超限报错
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
