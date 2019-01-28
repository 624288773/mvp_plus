package top.mvpplus.global.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import top.mvp.mvpplus.R;


/**
 * Created by zzh on 2018/5/16.
 */
public class TitleBar extends LinearLayout {
    private View rootView;
    private TextView tvTitle;
    private RelativeLayout btnBack;
    private TextView tvTipsBack;
    private ImageView ivBack;
    private RelativeLayout btnRight;
    private TextView tvTipsRight;
    private ImageView ivRight;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(final Context context, AttributeSet attrs) {
        rootView = View.inflate(context, R.layout.titlebar, this);
        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);
        tvTipsBack = findViewById(R.id.tvTipsBack);
        ivBack = findViewById(R.id.ivBack);
        btnRight = findViewById(R.id.btnRight);
        tvTipsRight = findViewById(R.id.tvTipsRight);
        ivRight = findViewById(R.id.ivRight);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        int backVisibility = array.getInt(R.styleable.TitleBar_btnBackVisibility, View.VISIBLE);
        int backRes = array.getResourceId(R.styleable.TitleBar_backRes, R.color.transparent);
        int rightVisibility = array.getInt(R.styleable.TitleBar_btnRightVisibility, View.VISIBLE);
        int rightRes = array.getResourceId(R.styleable.TitleBar_rightRes, R.color.transparent);
        String title = array.getString(R.styleable.TitleBar_text);
        array.recycle();
        btnBack.setVisibility(backVisibility);
        ivBack.setImageResource(backRes);
        btnRight.setVisibility(rightVisibility);
        ivRight.setImageResource(rightRes);
        tvTitle.setText(title);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void onBackCLick(View.OnClickListener listener) {
        btnBack.setOnClickListener(listener);
    }

    public void backTips(int tipsCount) {
        if (tipsCount > 0) {
            tvTipsBack.setVisibility(VISIBLE);
            tvTipsBack.setText(String.valueOf(tipsCount));
        } else {
            tvTipsBack.setVisibility(GONE);
            tvTipsBack.setText("0");
        }
    }

    public void onRightCLick(View.OnClickListener listener) {
        btnBack.setOnClickListener(listener);
    }

    public void rightTips(int tipsCount) {
        if (tipsCount > 0) {
            tvTipsRight.setVisibility(VISIBLE);
            tvTipsRight.setText(String.valueOf(tipsCount));
        } else {
            tvTipsRight.setVisibility(GONE);
            tvTipsRight.setText("0");
        }
    }
}
