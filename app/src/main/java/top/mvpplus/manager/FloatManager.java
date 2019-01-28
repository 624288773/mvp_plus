package top.mvpplus.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import top.mvpplus.global.utils.PermissionUtil;
import top.mvpplus.global.widget.FloatView;

/**
 * Created by Administrator on 2016/8/25.
 */
public class FloatManager {
    private FloatView floatView;
    private WindowManager.LayoutParams layoutParams;
    private static FloatManager instance;
    private boolean isOpenFloatWindow = false;
    private Context context;

    private FloatManager() {
    }

    public static FloatManager getInstance() {
        if (instance == null) {
            synchronized (FloatManager.class) {
                if (instance == null) {
                    instance = new FloatManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public void showView() {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (floatView == null) {
            floatView = new FloatView(context);
        }
        if (layoutParams == null) {
            int screenWidth = manager.getDefaultDisplay().getWidth();
            int screenHeight = manager.getDefaultDisplay().getHeight();
            layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;//StringUtil.dip2Px(context,70);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;//StringUtil.dip2Px(context,130);
            layoutParams.x = screenWidth + 200;
            layoutParams.y = 200;
        }
        floatView.setParams(layoutParams);
        manager.addView(floatView, layoutParams);
    }

    public void removeView(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.removeView(floatView);
    }

    public void openFloatWindow(FloatWindowListener listener) {
        if (!isOpenFloatWindow) {
            if (PermissionUtil.isFloatWindowEnable(context)) {
                isOpenFloatWindow = true;
                showView();
            } else {
                listener.onFailed();
            }
        }
    }

    //想重写dialog的,直接复制这个方法,修改onFailed()里面的dialog
    public void open(final Context context) {
        FloatManager.getInstance().openFloatWindow(new FloatManager.FloatWindowListener() {

            @Override
            public void onFailed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("请打开悬浮窗权限");
                builder.setPositiveButton("前去开启", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PermissionUtil.goFloatWindowSetting(context);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public interface FloatWindowListener {
        void onFailed();
    }
}