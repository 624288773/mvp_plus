package top.mvpplus.global.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import top.mvp.mvpplus.R;


/**
 * Created by Administrator on 2017/12/3 0003.
 */

public class DialogWait extends Dialog {
    public DialogWait(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DialogWait(@NonNull Context context) {
        this(context, R.style.Dialog);
        setContentView(R.layout.dialog_wait);
    }
}
