package top.mvpplus.ui.fragment;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import top.mvpplus.global.utils.PermissionUtil;
import top.mvpplus.manager.NotificationsManager;


/**
 * Created by zzh on 2018/8/17.
 */
public class BaseBindService extends Service {

    private ServiceConnection conn;
    private ServiceListener serviceListener;
    private MyBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        public BaseBindService getService() {
            return BaseBindService.this;
        }
    }

    public void startService(Context context, ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
        conn = new MyConnection();
        context.bindService(new Intent(context, BaseBindService.class), conn, context.BIND_AUTO_CREATE);//绑定存在自动创建
    }

    public void stopService(Context context) {
        context.unbindService(conn);
    }

    //想重写dialog的,直接复制这个方法,修改onFailed()里面的dialog
    public void open(final Context context) {
        NotificationsManager.getInstance().openNotification(context, new NotificationsManager.ForegroundListener() {

            @Override
            public void onSuccess() {
                myBinder.getService().startForeground(android.os.Process.myPid(), NotificationsManager.getInstance().getNotification());
            }

            @Override
            public void onFailed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("请打开通知栏权限")
                        .setPositiveButton("前去开启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PermissionUtil.goNotificationSetting(context);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }
        });
    }

    public class MyConnection implements ServiceConnection {

        @Override//当服务与Activity 连接时建立
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //只有当service onbind方法返回值不为null 调用
            Log.e("TAG", "onServiceConnected");
            myBinder = (MyBinder) binder;
            serviceListener.onSuccess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("TAG", "onServiceDisconnected");
        }
    }

    public interface ServiceListener {
        void onSuccess();
    }
}
