package ru.disdev.network.packets.out;

import ru.disdev.network.packets.ClientPacket;
import ru.disdev.network.packets.components.OutboundPacketsKeys;

/**
 * Created by DisDev on 25.07.2016.
 */
public class CreateGroupRequest extends ClientPacket {

    private final String userId;
    private final String universityName;
    private final String groupName;

    public CreateGroupRequest(String userId, String universityName, String groupName) {
        this.userId = userId;
        this.universityName = universityName;
        this.groupName = groupName;
    }
    @Override
    public void encode() {
        writeString(userId);
        writeString(universityName);
        writeString(groupName);
    }

    @Override
    public byte key() {
        return OutboundPacketsKeys.CREATE_GROUP_REQUEST;
    }
}
