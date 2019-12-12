package com.kyy.net;;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by kangyouyin on 2019/12/10.
 */
public class Server {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup(2);

        ServerBootstrap sb = new ServerBootstrap();

        try {
            ChannelFuture cf = sb.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .bind(8888)
                    .sync();

            System.out.println("server started!");
            ServerFrame.INSTANCE.updateServerMsg("server started!");
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

//    public static void main(String[] args) {
//        Server server = new Server();
//        server.start();
//    }

}

class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        System.out.println("initChannel");
        ch.pipeline()
                .addLast(new MsgEncode())
                .addLast(new MsgDecode())
                .addLast(new ServerHandler());
    }
}

class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelActive" + ctx.channel());
        Server.clients.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerFrame.INSTANCE.updateClientMsg(msg.toString());
        Server.clients.writeAndFlush(msg);

//        System.out.println("channelRead：" + msg);
//        ByteBuf buf = null;
//        try {
//            buf = (ByteBuf) msg;
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.getBytes(buf.readerIndex(), bytes);
//            System.out.println(new String(bytes));
//            Server.clients.writeAndFlush(Unpooled.copiedBuffer("你们好！".getBytes()));
//        } finally {
//            if (buf != null && buf.refCnt() != 0) {
//                ReferenceCountUtil.release(buf);
//            }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ServerFrame.INSTANCE.updateServerMsg(cause.getMessage());
        cause.printStackTrace();
        Server.clients.remove(ctx);
        ctx.close();
    }
}
