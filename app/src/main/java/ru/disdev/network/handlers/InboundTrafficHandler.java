package ru.disdev.network.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.ConnectionHolder;

/**
 * Created by Dislike on 18.07.2016.
 */
public class InboundTrafficHandler extends SimpleChannelInboundHandler<ServerPacket> {

    private final ConnectionHolder connectionHolder;

    public InboundTrafficHandler(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerPacket msg) throws Exception {
        msg.execute();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connectionHolder.disconnectIfEqual(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connectionHolder.setActiveChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionHolder.disconnectIfEqual(ctx.channel());
    }

}
