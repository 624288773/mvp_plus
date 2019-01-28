package top.mvpplus.manager;

import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import top.mvpplus.bean.net.SocketMsg;
import top.mvpplus.global.Const;
import top.mvpplus.global.SiteWebSocket;
import top.mvpplus.global.utils.JsonUtils;
import top.mvpplus.global.utils.LogUtils;

/**
 * Created by zzh on 2018/8/27.
 */

public class SocketManager {
    private static SocketManager instance;

    private SocketManager() {
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }

    //*************测试[WebSocket]start****************
    private SiteWebSocket siteWebsocket;

    public void connectWebSocket(String uid) {//传入uid,连接服务器
        if (siteWebsocket == null) {
            try {
                siteWebsocket = new SiteWebSocket(new URI(Const.BASE_WEB_SOCKET + "?uid=" + uid), new Draft_6455());
                siteWebsocket.connectBlocking();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendWebSocket("报告非凡哥，警署有规定嘎， 警员屋企有紧要事，有权请事假嘎。");
    }

    public void sendWebSocket(String msg) {
        siteWebsocket.send(msg);
    }
    //*************测试[WebSocket]end****************


    //*************测试[Netty]start****************
    private Socket socket;

    public void connectSocket(int uid) {//传入uid,连接服务器
        try {
            if (socket != null && socket.connected()) {
                return;
            }
            IO.Options options = new IO.Options();
            options.reconnectionAttempts = 7;     // 重连尝试次数
            options.reconnectionDelay = 3000;     // 失败重连的时间间隔(ms)
            options.timeout = 10000;              // 连接超时时间(ms)
            options.forceNew = true;
            options.transports = new String[]{"websocket"};
            //options.query = "uid=" + LoginManager.getInstance().getUser().getUid();
            options.query = "uid=" + uid;
            socket = IO.socket(Const.BASE_HOST + ":8090/", options);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("连接成功EVENT_CONNECT");
                    sendMsg("我连接了...");
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("连接错误CONNECT_ERROR");
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("连接超时CONNECT_TIMEOUT");
                }
            }).on(Socket.EVENT_PING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("Socket.EVENT_PING");
                }
            }).on(Socket.EVENT_PONG, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("Socket.EVENT_PONG");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("连接断开EVENT_DISCONNECT");
                }
            }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.w("收到:" + Arrays.toString(args));
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMsg(String message) {
        SocketMsg socketMsg = new SocketMsg();
        LogUtils.i("发送:" + JsonUtils.toJson(socketMsg));
        socket.emit(Socket.EVENT_MESSAGE, JsonUtils.toJson(socketMsg), new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.i("发送收到:" + Arrays.toString(args));
            }
        });
    }
    //*************测试[Netty]end****************
}
