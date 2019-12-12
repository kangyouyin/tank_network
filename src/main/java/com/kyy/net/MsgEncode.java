package com.kyy.net;;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by kangyouyin on 2019/12/11.
 */
public class MsgEncode extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf buf) throws Exception {
        buf.writeInt(msg.getMsgType().ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
}
