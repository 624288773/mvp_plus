package top.mvpplus.global.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import top.mvp.mvpplus.R;
import top.mvpplus.global.utils.AppUtils;


@SuppressLint("HandlerLeak")
public class ItemView extends RelativeLayout {

    private ImageView icon;
    private TextView count;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private CheckBox cb;
    private View line;

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param context
     * @param attrs
     */
    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_view, this, true);
        icon = findViewById(R.id.icon);
        count = findViewById(R.id.count);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        cb = findViewById(R.id.cb);
        line = findViewById(R.id.line);
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);//
        int iconRes = tArray.getResourceId(R.styleable.ItemView_iconRes, R.color.transparent);
        int iconVisibility = tArray.getInt(R.styleable.ItemView_iconVisibility, View.VISIBLE);
        String tv1String = tArray.getString(R.styleable.ItemView_text1);
        String tv2String = tArray.getString(R.styleable.ItemView_text2);
        int tv1Color = tArray.getResourceId(R.styleable.ItemView_text1Color, R.color.gray3);
        int tv2Color = tArray.getResourceId(R.styleable.ItemView_text2Color, R.color.gray3);
        String tv3String = tArray.getString(R.styleable.ItemView_text3);
        String tv4String = tArray.getString(R.styleable.ItemView_text4);
        int btn1Visibility = tArray.getInt(R.styleable.ItemView_button1Visibility, View.GONE);
        boolean cbCheck = tArray.getBoolean(R.styleable.ItemView_cbChecked, false);
        int cbVisibility = tArray.getInt(R.styleable.ItemView_cbVisibility, View.GONE);
        int lineVisibility = tArray.getInt(R.styleable.ItemView_lineVisibility, View.VISIBLE);
        text1.setText(tv1String);
        text1.setTextColor(ContextCompat.getColor(context, tv1Color));
        if (TextUtils.isEmpty(tv2String)) {
            text2.setVisibility(GONE);
        } else {
            text2.setText(tv2String);
            text2.setTextColor(ContextCompat.getColor(context, tv2Color));
        }
        text3.setText(tv3String);
        text4.setText(tv4String);
        text3.setVisibility(btn1Visibility);
        icon.setImageResource(iconRes);
        icon.setVisibility(iconVisibility);
        cb.setChecked(cbCheck);
        cb.setVisibility(cbVisibility);
        line.setVisibility(lineVisibility);
        tArray.recycle();
    }

    public ItemView(Context context) {
        super(context);
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIconRes(int resId) {
        icon.setImageResource(resId);
    }

    public void setCount(String str) {
        count.setText(str);
    }

    public void setCountVisibility(int visibility) {
        count.setVisibility(visibility);
    }

    public String getText1() {
        return text1.getText().toString();
    }

    public void setText1(String str) {
        text1.setText(str);
    }

    public void setText1Color(int resId) {
        text1.setTextColor(ContextCompat.getColor(AppUtils.getAppContext(), resId));
    }

    public String getText2() {
        return text2.getText().toString();
    }

    public void setText2(String str) {
        text2.setText(str);
    }

    public void setText2Color(int resId) {
        text2.setTextColor(ContextCompat.getColor(AppUtils.getAppContext(), resId));
    }

    public void setText3(String str) {
        text3.setText(str);
    }

    public String getText3() {
        return text3.getText().toString();
    }

    public void setText3Bg(int resId) {
        text3.setBackgroundResource(resId);
    }

    public void setText3Visibility(int visibility) {
        text3.setVisibility(visibility);
    }

    public void setText4Text(String str) {
        text4.setText(str);
    }

    public String getText4Text() {
        return text4.getText().toString();
    }

    public void setText4Bg(int resId) {
        text4.setBackgroundResource(resId);
    }

    public boolean isChecked() {
        return cb.isChecked();
    }

    public void setChecked(boolean checked) {
        cb.setChecked(checked);
    }
}
