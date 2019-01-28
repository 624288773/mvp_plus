package top.mvpplus.global.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatView extends LinearLayout {
    private static int statusBarHeight;//记录系统状态栏的高度
    private float xInScreen;//记录当前手指位置在屏幕上的横坐标值
    private float yInScreen;//记录当前手指位置在屏幕上的纵坐标值
    private float xDownInScreen;//记录手指按下时在屏幕上的横坐标的值
    private float yDownInScreen;//记录手指按下时在屏幕上的纵坐标的值
    private float xInView;//记录手指按下时在小悬浮窗的View上的横坐标的值
    private float yInView;//记录手指按下时在小悬浮窗的View上的纵坐标的值
    private int screenWidth;//屏幕宽度
    private WindowManager windowManager;//用于更新小悬浮窗的位置
    private WindowManager.LayoutParams mParams;//小悬浮窗的参数

    public FloatView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        //view = View.inflate(context,R.layout.float_view_layout,this);
        //layout = view.findViewById(R.id.layout_float);
        TextView textView = new TextView(context);
        textView.setText("悬浮窗");
        this.addView(textView);
        setOnTouchListener(new PttTouchEvent());
    }

    public FloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    class PttTouchEvent implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                    xInView = event.getX();
                    yInView = event.getY();
                    xDownInScreen = event.getRawX();
                    yDownInScreen = event.getRawY() - 56;
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY() - 56;
                    break;
                case MotionEvent.ACTION_MOVE:
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY() - 56;
                    // 手指移动的时候更新小悬浮窗的位置
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP:
                    // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                    if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    } else if (event.getRawX() > screenWidth / 2) {
                        mParams.x = screenWidth;
                        windowManager.updateViewLayout(FloatView.this, mParams);
                    } else if (event.getRawX() <= screenWidth / 2) {
                        mParams.x = 0;
                        windowManager.updateViewLayout(FloatView.this, mParams);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }


    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(FloatView.this, mParams);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
}
