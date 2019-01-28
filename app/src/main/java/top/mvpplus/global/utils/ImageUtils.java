package top.mvpplus.global.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import top.mvp.mvpplus.BuildConfig;
import top.mvp.mvpplus.R;
import top.mvpplus.global.Const;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class ImageUtils {

    public static String IMAGE_DIR;//压缩后图片目录
    public static String CACHE_CROP_PATH;//剪切图片的路径
    public static final String IMG_PATH = "img";
    public static final String RECORD_PATH = "record";

    public static void init() {
        File imgDir = FileUtils.getAppFile("img");
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        File recordDir = FileUtils.getAppFile("record");
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }
        IMAGE_DIR = imgDir.getAbsolutePath();
        CACHE_CROP_PATH = IMAGE_DIR + "/cache_crop.jpg";
    }

    /**
     * 压缩图片 Listener 方式
     */
    public static void compress(String photoPath, OnCompressListener listener) {
        compress(photoPath, 80, 0, 60, listener);
    }

    /**
     * 压缩图片 Listener 方式
     */
    public static void compress(String photoPath, int ignore, int gear, int quality, OnCompressListener listener) {
        Luban.with(AppUtils.getAppContext())
                .load(photoPath)
                .ignoreBy(ignore)
                .setTargetDir(FileUtils.makeDir(IMAGE_DIR))
                .setCompressListener(listener)
                .putGear(gear)
                .launch();
    }

    public static void showResult(File file) {
        int[] thumbSize = computeSize(file.getAbsolutePath());
        LogUtils.i("参数= " + thumbSize[0] + "X" + thumbSize[1] + " size:" + file.length());
    }

    private static int[] computeSize(String srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;

        return size;
    }


    public static Uri getCacheUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(AppUtils.getAppContext(), BuildConfig.APPLICATION_ID + ".fileprovider", new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    //调用系统的图片裁剪,输出200X200
    public static Intent cropImage(String path) {
        return cropImage(path, 1, 1, 216, 216);
    }

    /**
     * 调用系统的图片裁剪
     *
     * @param path 图片的路径
     */
    public static Intent cropImage(String path, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = null;
        try {
            intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(getCacheUri(path), "image/*");
            // 是否裁剪
            intent.putExtra("crop", "true");
            // 设置xy的裁剪比例1:1
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            // 设置输出的200X200像素
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            //是否缩放
            intent.putExtra("scale", false);
            //输入预裁剪图片的Uri，指定以后，可以通过这个Uri获得图片
            File file = FileUtils.makeFile(CACHE_CROP_PATH);
            Uri imageCropedCacheUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropedCacheUri);
            //是否返回图片数据可以不用，直接用Uri就可以
            intent.putExtra("return-data", false);
            // 设置输出图片格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // 是否关闭面部识别
            intent.putExtra("noFaceDetection", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }

    public static void load(String url, ImageView view) {
        load(url, view, R.mipmap.ic_launcher, false);
    }

    public static void load(String url, ImageView view, int errType, boolean isCircle) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(AppUtils.getAppContext()).load(R.mipmap.ic_launcher).into(view);
        } else {
            String tagUrl = Const.BASE_HOST + Const.IMG_PATH + url;
            LogUtils.i("tagUrl = " + tagUrl);
            RequestOptions options = new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).error(errType);
            if (isCircle) {
                options.circleCrop();
            }
            Glide.with(AppUtils.getAppContext()).load(tagUrl)
                    .apply(options).into(view);
        }
    }
}