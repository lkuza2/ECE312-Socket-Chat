package com.ece312.socketchat.server;

import com.ece312.socketchat.util.MainUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

/**
 * Created by kuzalj on 12/15/2016.
 */
public class MainServerHandler extends ChannelInboundHandlerAdapter {


    private boolean nameRead = false;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        MainUtil.getInstance().getBroadcast().add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MainUtil.getInstance().getBroadcast().remove(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf in = (ByteBuf) msg;
        StringBuilder buffer = new StringBuilder();
        try {
            while (in.isReadable()) { // (1)
                char read = (char) in.readByte();
                if (read != '\n') {
                    buffer.append(read);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
        System.out.println();
        if (!nameRead) {
            System.out.print("Connection established with " + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + " (" +
                    buffer.toString().replace("<", "").replace(">", "").trim() + ")");
            nameRead = true;
        } else if (buffer.toString().split(">")[1].trim().equals("exit")) {
            MainUtil.getInstance().exit();
        } else {
            System.out.print(buffer.toString());
            MainUtil.getInstance().sendData(buffer.toString(), ctx.channel());
        }
        System.out.println();
        MainUtil.getInstance().printCursor();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
