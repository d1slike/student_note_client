package ru.disdev.network.packets;

import io.netty.buffer.ByteBuf;

/**
 * Created by Dislike on 18.07.2016.
 */
public abstract class AbstractPacket {
    protected ByteBuf buffer;

    public final void setBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public abstract byte key();

}
