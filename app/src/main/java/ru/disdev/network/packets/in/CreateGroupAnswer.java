package ru.disdev.network.packets.in;

import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.components.InboundPacketsKeys;

/**
 * Created by DisDev on 25.07.2016.
 */
public class CreateGroupAnswer extends ServerPacket {

    private long groupId;

    @Override
    public void execute() {

    }

    @Override
    public void decode() {
        groupId = readLong();
    }

    @Override
    public byte key() {
        return InboundPacketsKeys.CREATE_GROUP_ANSWER;
    }

    public long getGroupId() {
        return groupId;
    }

    public boolean isSuccess() {
        return groupId != -1;
    }
}
