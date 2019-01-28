package top.mvpplus.global.utils;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12 0012.
 */

public class LogUtils {
    static String className;
    static String methodName;
    static int lineNumber;
    private static boolean DEBUG = true;
    private static StringBuilder stringBuffer = new StringBuilder();

    public static void save(int action) {
        save(String.valueOf(action));
    }

    public static void save(String message) {
        if (DEBUG) {
            getNames(new Throwable().getStackTrace());
            stringBuffer.setLength(0);
            stringBuffer.append(System.currentTimeMillis()).append("@").append(className).append("#").append(methodName)
                    .append("{").append(lineNumber).append("}").append(message);
            Log.w(className, stringBuffer.toString());
            try {
                if (true) {//开启日志写到文件 Android/<PackageName>/files/log/log.txt
                    File logDir = AppUtils.getAppContext().getExternalFilesDir("log");
                    File file = FileUtils.makeDirFile(FileUtils.makeDir(logDir.getAbsolutePath()) + "/log.txt");
                    FileWriter fos = new FileWriter(file, true);
                    fos.write(stringBuffer.append("\n").toString());
                    fos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void i(String message) {
        if (!DEBUG)
            return;
        getNames(new Throwable().getStackTrace());
        Log.i(className, getString(message));
    }

    public static void w(String message) {
        if (!DEBUG)
            return;
        getNames(new Throwable().getStackTrace());
        Log.w(className, getString(message));
    }

    public static void e(String message) {
        if (!DEBUG)
            return;
        getNames(new Throwable().getStackTrace());
        Log.e(className, getString(message));
    }

    private static String getString(String message) {
        stringBuffer.setLength(0);
        stringBuffer.append(methodName).append("[").append(lineNumber).append("]>").append(message);
        return stringBuffer.toString();
    }

    private static void getNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }
}
