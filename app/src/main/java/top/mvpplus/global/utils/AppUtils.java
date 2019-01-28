package top.mvpplus.global.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.net.TrafficStats;
import android.os.PowerManager;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import top.mvpplus.manager.CrashManager;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class AppUtils {
    private static Context mContext;

    public static void initUtils(Context context){
        mContext = context;
        ToastUtils.init();
        CrashManager.getInstance().init();
        ImageUtils.init();
    }

    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 打开资源的配置文件
     *
     * @param fileName The name of the asset to open. This name can be hierarchical.
     * @return
     */
    public static Properties getAssetsProperties(String fileName) {
        if (mContext == null || TextUtils.isEmpty(fileName)) {
            return null;
        }
        Properties properties = new Properties();
        try {
            InputStreamReader in = new InputStreamReader(mContext.getResources().getAssets().open(fileName));
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    /**
     * 获取meta-data值
     *
     * @return
     */
    public static String getMetaData(String metaDataKey) {
        ApplicationInfo info = null;
        try {
            PackageManager pm = mContext.getPackageManager();
            info = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            return String.valueOf(info.metaData.get(metaDataKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean canIntent(Intent intent) {
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        return isIntentSafe;
    }

    /**
     *  如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
     *  权限<uses-permission android:name="android.permission.RECEIVE_USER_PRESEN" />
     * @return
     */
    public static boolean isScreenOn(){
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }
    /**
     *摄像头是否有效
     * @param context
     * @return
     */
    public static boolean isCameraValid(Context context){
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            mCamera.release();
            mCamera=null;
        } catch (Exception e) {
            canUse = false;
        }
        return canUse;
    }

    /**
     * 唤醒屏幕
     * @param context
     */
    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    public static void getFlow(final String s) {
        final long rx = TrafficStats.getUidRxBytes(android.os.Process.myUid());
        final long tx = TrafficStats.getUidTxBytes(android.os.Process.myUid());
        LogUtils.i(s + "流量: rx=" + rx + "B  ,tx=" + tx + "B");
    }
}
