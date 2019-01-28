package top.mvpplus.global.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/1/20 0020.
 */

public class HandlerUtils {

    public static class HandlerHolder extends Handler {
        WeakReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }


    public static void sendMessage(Handler handler, int what) {
        removeHandlerMsg(handler, what);
        handler.obtainMessage(what).sendToTarget();
    }

    public static void sendMessage(Handler handler, int what, Object obj) {
        removeHandlerMsg(handler, what);
        handler.obtainMessage(what, obj).sendToTarget();
    }

    public static void sendMessage(Handler handler, int what, Object obj, int arg1, int arg2) {
        removeHandlerMsg(handler, what);
        handler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
    }

    public static void sendMessage(Handler handler, int what, int arg1, int arg2) {
        removeHandlerMsg(handler, what);
        handler.obtainMessage(what, arg1, 0).sendToTarget();
    }

    public static void sendMessage(Handler handler, Message message) {
        removeHandlerMsg(handler, message.what);
        handler.sendMessage(message);
    }

    public static void sendMessageDelay(Handler handler, int what, long delayMillis) {
        removeHandlerMsg(handler, what);
        handler.sendEmptyMessageDelayed(what, delayMillis);
    }

    public static void sendMessageDelay(Handler handler, int what, int arg1, long delayMillis) {
        removeHandlerMsg(handler, what);
        handler.sendMessageDelayed(handler.obtainMessage(what, arg1, 0), delayMillis);
    }

    public static void sendMessageDelay(Handler handler, int what, Object obj, long delayMillis) {
        removeHandlerMsg(handler, what);
        handler.sendMessageDelayed(handler.obtainMessage(what, obj), delayMillis);
    }

    public static void sendMessageDelay(Handler handler, Message message, long delayMillis) {
        removeHandlerMsg(handler, message.what);
        handler.sendMessageDelayed(message, delayMillis);
    }

    public static void removeHandlerMsg(Handler handler, int what) {
        if (null == handler) {
            return;
        }
        if (handler.hasMessages(what)) {
            handler.removeMessages(what);
        }
    }

    public static void removeCallbacksAndMessages(Handler handler) {
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
