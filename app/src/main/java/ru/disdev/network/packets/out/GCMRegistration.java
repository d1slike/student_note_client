package ru.disdev.network.packets.out;

import ru.disdev.network.packets.ClientPacket;

/**
 * Created by Dislike on 19.07.2016.
 */
public class GCMRegistration extends ClientPacket {

    private final String registrationId;
    private final long userId;

    public GCMRegistration(String registrationId, long userId) {
        this.registrationId = registrationId;
        this.userId = userId;
    }

    @Override
    public void encode() {
        writeString(registrationId);
        writeLong(userId);
    }

    @Override
    public byte key() {
        return 11;
    }
}
