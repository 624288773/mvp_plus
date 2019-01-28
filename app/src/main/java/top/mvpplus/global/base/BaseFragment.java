package top.mvpplus.global.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.io.Serializable;
import java.util.Map;

import top.mvpplus.global.utils.HandlerUtils;
import top.mvpplus.global.widget.DialogWait;


/**
 * Created by zzh on 2018/5/16.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView, HandlerUtils.OnReceiveMessageListener {
    private static final int MSG_SHOW_DIALOG = 100;
    private static final int MSG_DISMISS_DIALOG = 101;
    public HandlerUtils.HandlerHolder mHandler;
    public P mPresenter;
    public BaseFragment mActivity;
    private Dialog mDialog;
    public boolean immersive = false;

    protected abstract P initPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mHandler = new HandlerUtils.HandlerHolder(this);
        mPresenter = initPresenter();
        if (mPresenter != null)
            mPresenter.onAttach(this);
        if (immersive) {
            initImmersive();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);//隐藏软键盘
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {//dialog正在显示而且该activity并没有结束才关闭dialog
            if (mDialog.isShowing() && getActivity() != null && !getActivity().isFinishing()) {//dialog正在显示而且该activity并没有结束才关闭dialog
                mDialog.dismiss();
            }
        }
        if (mPresenter != null)
            mPresenter.onDetach();
        HandlerUtils.removeCallbacksAndMessages(mHandler);
    }

    /**
     * 初始化是否要通知栏透明,必须要在 super.onCreate() 前调用
     *
     * @param immersive
     */
    public void initImmersive(boolean immersive) {
        this.immersive = immersive;
    }

    //通知栏透明
    private void initImmersive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    //等待框
    private void initDialog() {
        if (mDialog == null) {
            mDialog = new DialogWait(getActivity());
        }
    }

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_DIALOG:
                initDialog();
                if (!mDialog.isShowing() && !getActivity().isFinishing()) {
                    mDialog.show();
                }
                break;
            case MSG_DISMISS_DIALOG:
                if (mDialog != null) {
                    if (mDialog.isShowing() && !getActivity().isFinishing()) {//dialog正在显示而且该activity并没有结束才关闭dialog
                        mDialog.dismiss();
                    }
                }
                break;
        }
    }

    /**
     * 显示dialog
     */
    public void showDialog() {
        HandlerUtils.sendMessage(mHandler, MSG_SHOW_DIALOG);
    }

    /**
     * 隐藏dialog
     */
    public void dismissDialog() {
        HandlerUtils.sendMessage(mHandler, MSG_DISMISS_DIALOG);
    }

    @Override
    public void finishActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 跳转Activity没传参数
     *
     * @param clazz 跳转的类
     */
    public void goActivity(Class<? extends Activity> clazz) {
        goActivity(clazz, null);
    }

    /**
     * 跳转Activity有传参数
     *
     * @param clazz  跳转的类
     * @param extras 带的参数
     */
    public void goActivity(Class<? extends Activity> clazz, Map<String, Object> extras) {
        Bundle bundle = null;
        if (extras != null && extras.size() > 0) {
            bundle = new Bundle();
            for (Map.Entry<String, Object> entry : extras.entrySet()) {
                String key = entry.getKey();
                Object extra = entry.getValue();
                if (extra instanceof String) {
                    bundle.putString(key, (String) extra);
                } else if (extra instanceof Integer) {
                    bundle.putInt(key, (int) extra);
                } else if (extra instanceof Long) {
                    bundle.putLong(key, (long) extra);
                } else if (extra instanceof Boolean) {
                    bundle.putBoolean(key, (boolean) extra);
                } else if (extra instanceof Double) {
                    bundle.putDouble(key, (double) extra);
                } else if (extra instanceof Float) {
                    bundle.putFloat(key, (float) extra);
                } else {
                    bundle.putSerializable(key, (Serializable) extra);
                }
            }
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        if (bundle != null && !bundle.isEmpty()) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}