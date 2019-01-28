package top.mvpplus.global.utils;

/**
 * Created by Administrator on 2017/1/12 0012.
 */


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 跟网络相关的工具类
 */
public class NetUtils {
    /**
     * 判断网络是否连接
     *
     * @param
     * @return
     */
    public static boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) AppUtils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != manager) {
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (null != activeInfo && activeInfo.isConnected()) {//如果无网络连接activeInfo为null
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) AppUtils.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 判断是否是移动网连接
     */
    public static boolean isMobile() {
        ConnectivityManager cm = (ConnectivityManager) AppUtils.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;

    }

    /**
     * 网连接类型
     */
    public static int netType() {
        ConnectivityManager manager = (ConnectivityManager) AppUtils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != manager) {
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (null != activeInfo && activeInfo.isConnected()) {//如果无网络连接activeInfo为null
                return manager.getActiveNetworkInfo().getType();
            }
        }
        return -1;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}