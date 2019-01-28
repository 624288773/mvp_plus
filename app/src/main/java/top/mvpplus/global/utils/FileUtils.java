package top.mvpplus.global.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class FileUtils {
    /**
     * 创建一个文件
     *
     * @param filePath 需要创建的文件路径
     */
    public static File makeFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    return f;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return f;
    }

    public static boolean copyFile(File srcFile, File saveFile) {
        boolean flag = false;
        try {
            //获得原文件流
            FileInputStream inputStream = new FileInputStream(srcFile);
            byte[] data = new byte[1024];
            //输出流
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            //开始处理流
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
            flag = true;
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public static String makeDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return path;
            }
        }
        return path;
    }

    public static File makeDirFile(String path) {
        File file = new File(path);
        makeDir(file.getParentFile().getAbsolutePath());
        return makeFile(path);
    }

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    deleteFile(f);
                }
            }
        }
    }

    public static boolean deleteFile(File f) {
        if (f.exists()) { // 判断是否存在
            deleteAllFiles(f);
            try {
                return f.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static File getAppFile(String s) {
        return AppUtils.getAppContext().getExternalFilesDir(s);
    }

    public static File getAppCache() {
        return AppUtils.getAppContext().getExternalCacheDir();
    }
}
