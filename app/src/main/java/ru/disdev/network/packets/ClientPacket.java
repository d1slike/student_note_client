package ru.disdev.network.packets;

/**
 * Created by Dislike on 18.07.2016.
 */
public abstract class ClientPacket extends AbstractPacket {
    public abstract void encode();

    protected final void writeInt(int value) {
        buffer.writeInt(value);
    }

    protected final void writeString(String value) {
        for (char c : value.toCharArray())
            buffer.writeChar(c);
        buffer.writeChar('\0');
    }

    protected final void writeLong(long value) {
        buffer.writeLong(value);
    }

    protected final void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    protected final void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    protected final void writeBoolean(boolean value) {
        buffer.writeBoolean(value);
    }


}
