package top.mvpplus.bean.net;

/**
 * Created by zzh on 2018/8/28.
 */

public class SocketMsg {
    private int type;
    private long sid;
    private long tid;
    private long time;
    private String msg;

    public SocketMsg() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MsgEvent{" +
                "type=" + type +
                ", sid=" + sid +
                ", tid=" + tid +
                ", time=" + time +
                ", msg='" + msg + '\'' +
                '}';
    }
}