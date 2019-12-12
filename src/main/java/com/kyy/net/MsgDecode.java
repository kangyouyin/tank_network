package com.kyy.net;;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by kangyouyin on 2019/12/11.
 */
public class MsgDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) {
            return;
        }

        in.markReaderIndex();
        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Msg msg = (Msg)Class.forName("com.kyy.net." + msgType + "Msg").getDeclaredConstructor().newInstance();
        msg.parse(bytes);
        out.add(msg);
    }
}
