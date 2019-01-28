package top.mvpplus.manager;


import top.mvpplus.global.utils.LogUtils;

/**
 * Created by Administrator on 2018/2/23.
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {
    private static CrashManager instance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashManager() {
    }

    public static CrashManager getInstance() {
        if (instance == null) {
            synchronized (CrashManager.class) {
                if (instance == null) {
                    instance = new CrashManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ex.toString());
        StackTraceElement[] elements = ex.getStackTrace();
        for (StackTraceElement element : elements) {
            stringBuilder.append(element.toString());
        }
        LogUtils.save(stringBuilder.toString());
        // 如果用户没有处理则让系统默认的异常处理器来处理
        mDefaultHandler.uncaughtException(thread, ex);
    }
}
