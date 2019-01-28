package top.mvpplus.global.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 需要先在主线程调用init()初始化
 */
public class ToastUtils {
    private static TextView mMessage;
    private static Toast mToast;
    private static Handler mHandler;

    public static void init() {
        if (null == mToast) {
            mToast = new Toast(AppUtils.getAppContext());
            mMessage = new TextView(AppUtils.getAppContext());
            mMessage.setTextColor(0XFFFFFFFF);
            mMessage.setBackgroundResource(android.R.drawable.toast_frame);
            mToast.setView(mMessage);
            mToast.setDuration( Toast.LENGTH_SHORT);
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    mMessage.setText((String) msg.obj);
                    mToast.show();
                }
            };
        }
    }


    public static synchronized void show(String text) {
        HandlerUtils.sendMessage(mHandler, 0, text);
    }

    public static synchronized void show(int resId) {
        show(AppUtils.getAppContext().getResources().getString(resId));
    }

    public static synchronized void cancel() {
        mToast.cancel();
    }
}