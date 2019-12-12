package com.kyy.net;;

/**
 * Created by kangyouyin on 2019/12/11.
 */
public abstract class Msg {
    public abstract void handle();
    public abstract byte[] toBytes();
    public abstract void parse(byte[] bytes);
    public abstract MsgType getMsgType();
}
