package top.mvpplus.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import top.mvpplus.global.utils.LogUtils;
import top.mvpplus.global.utils.NetUtils;

/*  Manifest配置
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<receiver android:name=".utils.NetChangeReceiver">
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
            </intent-filter>
</receiver>
*/
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netType = NetUtils.netType();
            LogUtils.i("netType = " + netType);
            if (netType == -1 || netType == 0) {
                LogUtils.i("网络断了");
            } else {
                LogUtils.i("恢复网络");
            }
        }
    }
}