package ru.disdev.network.packets.out;

import ru.disdev.network.packets.ClientPacket;
import ru.disdev.network.packets.components.OutboundPacketsKeys;

/**
 * Created by Dislike on 19.07.2016.
 */
public class GCMRegistration extends ClientPacket {

    private final String registrationId;
    private final String userId;

    public GCMRegistration(String registrationId, String userId) {
        this.registrationId = registrationId;
        this.userId = userId;
    }

    @Override
    public void encode() {
        writeString(registrationId);
        writeString(userId);
    }

    @Override
    public byte key() {
        return OutboundPacketsKeys.GCM_REGISTRATION_REQUEST;
    }
}
