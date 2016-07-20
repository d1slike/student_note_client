package ru.disdev.network;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.disdev.network.handlers.InboundTrafficHandler;
import ru.disdev.network.handlers.PacketDecoder;
import ru.disdev.network.handlers.PacketEncoder;
import ru.disdev.network.packets.ClientPacket;

/**
 * Created by Dislike on 19.07.2016.
 */
public class ConnectionHolder {

    private static final ConnectionHolder instance = new ConnectionHolder();

    public static ConnectionHolder getInstance() {
        return instance;
    }

    private final EventLoopGroup group = new NioEventLoopGroup(2);
    private volatile Channel activeChannel;
    private volatile boolean canMakeDisconnectNow;

    private ConnectionHolder() {

    }

    public synchronized boolean disconnectIfEqual(Channel channel) {
        boolean equal = activeChannel != null && activeChannel.equals(channel);
        if (equal)
            disconnect(true);
        return equal;
    }

    public synchronized void setActiveChannel(Channel activeChannel) {
        if (this.activeChannel != null)
            return;
        this.activeChannel = activeChannel;
    }

    public boolean isConnected() {
        return activeChannel != null;
    }

    public synchronized void shutdown() {
        group.shutdownGracefully();
    }

    public synchronized void disconnect(boolean orderDisconnect) {
        if (!isConnected())
            return;
        if (orderDisconnect || canMakeDisconnectNow) {
            activeChannel.close();
            activeChannel = null;
        }
    }

    public synchronized ConnectionHolder connect() {
        if (isConnected())
            return this;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                //.option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketEncoder(), new PacketDecoder(), new InboundTrafficHandler(ConnectionHolder.this));
                    }
                });
        try {
            bootstrap.connect("192.168.0.101", 9191).await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        return this;
    }

    public ConnectionHolder sendPacketToServer(final ClientPacket packet) {
        if (isConnected() && activeChannel.isWritable())
            activeChannel.writeAndFlush(packet);
        return this;
    }

    public void setCanMakeDisconnectNow(boolean canMakeDisconnectNow) {
        this.canMakeDisconnectNow = canMakeDisconnectNow;
    }
}
