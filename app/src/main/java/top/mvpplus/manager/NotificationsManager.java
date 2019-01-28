package top.mvpplus.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import top.mvp.mvpplus.R;
import top.mvpplus.global.utils.AppUtils;
import top.mvpplus.global.utils.PermissionUtil;
import top.mvpplus.ui.activity.MainActivity;


/**
 * Created by zzh on 2018/8/17.
 */
public class NotificationsManager {

    private static NotificationsManager instance;
    private android.app.NotificationManager notificationManager;

    public static NotificationsManager getInstance() {
        if (instance == null) {
            synchronized (NotificationsManager.class) {
                if (instance == null) {
                    instance = new NotificationsManager();
                }
            }
        }
        return instance;
    }

    public void openNotification(Context context, ForegroundListener listener) {
        if (PermissionUtil.isNotificationEnabled(context)) {
            listener.onSuccess();
        } else {
            listener.onFailed();
        }
    }

    public Notification getNotification() {
        Context context = AppUtils.getAppContext();
        Intent nfIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context.getApplicationContext(), android.os.Process.myPid(), nfIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        if (Build.VERSION.SDK_INT < 16) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
            notification = builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentIntent(contentIntent)
                    .build();// getNotification()
        } else {//>=16版本
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= 26) {
                //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
                if (null == notificationManager) {
                    notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                }
                NotificationChannel notificationChannel = new NotificationChannel(context.getPackageName(),
                        "允许通知", android.app.NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                builder = new Notification.Builder(context.getApplicationContext(), context.getPackageName());
            } else {
                builder = new Notification.Builder(context.getApplicationContext());
            }
            notification = builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentIntent(contentIntent)
                    .build();// getNotification()
        }
        return notification;
    }

    public interface ForegroundListener {
        void onSuccess();

        void onFailed();
    }
}
