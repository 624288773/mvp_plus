package top.mvpplus.manager;

import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import top.mvpplus.global.Const;
import top.mvpplus.global.utils.AppUtils;
import top.mvpplus.global.utils.LogUtils;

/**
 * Created by zzh on 2018/7/3.
 */

public class HttpManager {
    private static HttpManager instance;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();


    /**
     * 异步的Get请求
     *
     * @param url url
     */
    public void get(String url, Callback callback) {
        url = Const.BASE_HOST + url;
        LogUtils.i("Get请求=>"+url);
        // 创建一个Request
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        // 请求加入调度
        call.enqueue(callback);
    }

    /**
     * 异步的Post请求
     *
     * @param url    url
     * @param params Map params
     */
    public void post(String url, Map<String, String> params, Callback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        url = Const.BASE_HOST + url;
        FormBody.Builder builder = new FormBody.Builder();
        StringBuilder sb = new StringBuilder();
        sb.append("Post请求=>").append(url).append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {//在这对添加的参数进行遍历
            String value = entry.getValue() != null ? entry.getValue() : "";
            sb.append(entry.getKey()).append("=").append(value).append("&");
            builder.add(entry.getKey(), value);//把key和value添加到formBody中
        }
        LogUtils.i(sb.toString());
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    /**
     * @param url      下载连接
     * @param savePath 保存的路径
     * @param listener 下载监听
     */
    public void download(final String url, final String savePath, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath);//, getNameFromUrl(url));//如果过来的是文件夹就截取url后面的作为文件名.
                    LogUtils.i("file.path = " + file.getAbsolutePath());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        LogUtils.i("下载进度:已传=" + sum + "/总长=" + total + ",进度=" + progress);
                        // 下载中
                        listener.onDownloading(progress + 1);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public void upload(String uploadUrl, String filePath, final OnUploadListener listener) {
        File file = new File(filePath);
        String f = filePath.substring(filePath.lastIndexOf("/") + 1);//名称
        LogUtils.i("filePath = " + filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("Connection", "close")
                .addFormDataPart("Charset", "UTF-8")
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + Const.UPLOAD_FILE + "\"; filename=\"" + f + "\""), fileBody)
                .build();
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, listener);
        final Request request = new Request.Builder().url(uploadUrl).post(progressRequestBody).build();
        //开始请求
        LogUtils.i("开始请求 " + uploadUrl);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onUploadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onUploadSuccess(response.body().string());
            }
        });
    }


    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(AppUtils.getAppContext().getExternalCacheDir(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    public interface OnUploadListener {
        /**
         * 上传成功
         */
        void onUploadSuccess(String result);

        /**
         * 上传进度
         */
        void onUploading(int progress);

        /**
         * 上传失败
         */
        void onUploadFailed();
    }


    /**
     * 包装的请求体，处理进度
     */
    public class ProgressRequestBody extends RequestBody {
        //实际的待包装请求体
        private final RequestBody requestBody;
        //进度回调接口
        private final OnUploadListener progressListener;
        //包装完成的BufferedSink
        private BufferedSink bufferedSink;

        /**
         * 构造函数，赋值
         *
         * @param requestBody      待包装的请求体
         * @param progressListener 回调接口
         */
        public ProgressRequestBody(RequestBody requestBody, OnUploadListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        /**
         * 重写调用实际的响应体的contentType
         *
         * @return MediaType
         */
        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         *
         * @return contentLength
         * @throws IOException 异常
         */
        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        /**
         * 重写进行写入
         *
         * @param sink BufferedSink
         * @throws IOException 异常
         */
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                //包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            //写入
            requestBody.writeTo(bufferedSink);
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();

        }

        /**
         * 写入，回调进度接口
         *
         * @param sink Sink
         * @return Sink
         */
        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        //获得contentLength的值，后续不再调用
                        contentLength = contentLength();
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount;
                    //回调
                    if (progressListener != null) {
                        long progress = bytesWritten * 100 / contentLength;
                        LogUtils.i("上传进度:已传=" + bytesWritten + "/总长=" + contentLength + ",进度=" + progress);
                        progressListener.onUploading((int) progress);
                    }
                }
            };
        }
    }
}
