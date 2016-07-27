package ru.disdev.network;

import java.util.concurrent.Executor;
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
import ru.disdev.network.packets.ServerPacket;

/**
 * Created by Dislike on 19.07.2016.
 */
public class ConnectionHolder {

    private static final ConnectionHolder instance = new ConnectionHolder();

    public static ConnectionHolder getInstance() {
        return instance;
    }

    private final EventLoopGroup group = new NioEventLoopGroup(2);
    private final PacketWaiter waiter = new PacketWaiter();
    private final Bootstrap bootstrap = new Bootstrap()
            .channel(NioSocketChannel.class)
            .group(group)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new PacketEncoder(), new PacketDecoder(), new InboundTrafficHandler(ConnectionHolder.this));
                }
            });

    private volatile Channel activeChannel;

    private ConnectionHolder() {

    }

    public synchronized boolean disconnectIfEqual(Channel channel) {
        boolean equal = activeChannel != null && activeChannel.equals(channel);
        if (equal)
            disconnect();
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

    public synchronized void disconnect() {
        if (!isConnected())
            return;
        activeChannel.close();
        activeChannel = null;
    }

    public synchronized ConnectionHolder connect() {
        if (isConnected())
            return this;
        bootstrap.connect("5.166.229.183", 9191).awaitUninterruptibly(3, TimeUnit.SECONDS);
        return this;
    }

    public boolean sendPacketToServer(final ClientPacket packet) {
        if (isConnected() && activeChannel.isWritable()) {
            activeChannel.writeAndFlush(packet);
            return true;
        }
        return false;
    }

    public PacketWaiter sendPacketToServerAndWaitAnswer(final ClientPacket packet) {
        boolean success = sendPacketToServer(packet);
        waiter.setSuccessfullySentPackerToServer(success);
        return waiter;
    }

    public boolean checkForWaiting(ServerPacket packet) {
        return waiter.checkForWaitAndNotify(packet);
    }

    public Executor getExecutor() {
        return group;
    }
}
