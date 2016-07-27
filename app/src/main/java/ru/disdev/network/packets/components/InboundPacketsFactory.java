package ru.disdev.network.packets.components;

import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.in.AuthResponse;
import ru.disdev.network.packets.in.CreateGroupAnswer;

/**
 * Created by Dislike on 18.07.2016.
 */
public class InboundPacketsFactory {
    public static ServerPacket create(final byte key) {
        switch (key) {
            case InboundPacketsKeys.AUTH_RESPONSE:
                return new AuthResponse();
            case InboundPacketsKeys.CREATE_GROUP_ANSWER:
                return new CreateGroupAnswer();
        }
        return null;
    }
}
