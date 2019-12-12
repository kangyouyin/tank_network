package com.kyy.net;;

import com.kyy.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by kangyouyin on 2019/12/10.
 */
public class Client {
    public static final Client INSTANCE = new Client();

    private Channel channel = null;

    private Client() {
    }

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        try {
            ChannelFuture future = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost", 8888);

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture cf) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("connected!");
                        channel = cf.channel();
                    } else {
                        System.out.println("not connected!");
                    }

                }
            });

            future.sync();
            // wait until close
            future.channel().closeFuture().sync();
            System.out.println("connection closed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }


    public void send(Msg msg) {
        System.out.println("SEND:" + msg);
        channel.writeAndFlush(msg);
    }

//    public static void main(String[] args) {
//        Client.INSTANCE.connect();
//    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        System.out.println("initChannel");
        ch.pipeline()
                .addLast(new MsgEncode())
                .addLast(new MsgDecode())
                .addLast(new ClientHandler());
    }
}

class ClientHandler extends SimpleChannelInboundHandler<Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
//        System.out.println(msg);
        msg.handle();
//        System.out.println("channelRead0：" + o);
//        ByteBuf buf = null;
//        try {
//            buf = (ByteBuf) msg;
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.getBytes(buf.readerIndex(), bytes);
//            System.out.println(new String(bytes));
//        } finally {
//            if (buf != null && buf.refCnt() != 0) {
//                ReferenceCountUtil.release(buf);
//            }
//        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
//        System.out.println("channelActive：" + ctx.channel());
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello".getBytes()));
    }
}
