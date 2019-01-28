package top.mvpplus.global;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Date;

public class SiteWebSocket extends WebSocketClient {

    public SiteWebSocket(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("websocket","open connection");
    }

    @Override
    public void onMessage(String message) {
        Log.i("websocket","receive: "+message);
        Message msg = new Message();
        msg.obj = message;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("websocket","Connection close by "+(remote?"remote peer":"us") +" at "+new Date(System.currentTimeMillis()));
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
