package ru.disdev.network.packets.in;

import ru.disdev.model.UserInfo;
import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.components.InboundPacketsKeys;

/**
 * Created by DisDev on 22.07.2016.
 */
public class AuthResponse extends ServerPacket {

    private boolean success;
    private String userId;
    private long groupId = -1;
    private boolean canEditGroup;
    private boolean canMakeNewPost;

    @Override
    public void execute() {

    }

    @Override
    public void decode() {
        success = readBoolean();
        userId = readString();
        groupId = readLong();
        canEditGroup = readBoolean();
        canMakeNewPost = readBoolean();
    }

    @Override
    public byte key() {
        return InboundPacketsKeys.AUTH_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }

    public UserInfo makeUser(String name, String email) {
        return UserInfo.builder()
                .setId(userId)
                .setEmail(email)
                .setName(name)
                .setCanEditThisGroup(canEditGroup)
                .setCanMakeNewPost(canMakeNewPost)
                .setGroupId(groupId)
                .build();
    }

}
