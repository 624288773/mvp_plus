package top.mvpplus.global.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by DELL on 2018/3/31.
 */

public abstract  class BasePresenter<V extends BaseView> {
    private Reference<V> mViewReference = null;

    public void onAttach(V view) {
        mViewReference = new WeakReference<>(view);
    }

    public V getView() {
        if (mViewReference != null) {
            return mViewReference.get();
        }
        return null;
    }

    public boolean isAttach() {
        return null != mViewReference && null != mViewReference.get();
    }

    public void onDetach() {
        if (null != mViewReference) {
            mViewReference.clear();
            mViewReference = null;
        }
    }
}
