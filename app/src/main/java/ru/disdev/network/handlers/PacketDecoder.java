package ru.disdev.network.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.components.InboundPacketsFactory;

import java.util.List;

/**
 * Created by Dislike on 18.07.2016.
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final byte key = in.readByte();
        ServerPacket packet = InboundPacketsFactory.create(key);
        if (packet == null)
            return;
        packet.setBuffer(in);
        packet.decode();
        packet.setBuffer(null);
        out.add(packet);
    }
}
