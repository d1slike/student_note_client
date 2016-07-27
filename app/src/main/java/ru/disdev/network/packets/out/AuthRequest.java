package ru.disdev.network.packets.out;

import android.util.Log;

import ru.disdev.network.packets.ClientPacket;
import ru.disdev.network.packets.components.OutboundPacketsKeys;

/**
 * Created by DisDev on 22.07.2016.
 */
public class AuthRequest extends ClientPacket {
    private final String token;

    public AuthRequest(String token) {
        this.token = token;
        Log.i("token", token);
    }

    @Override
    public void encode() {
        writeString(token);
    }

    @Override
    public byte key() {
        return OutboundPacketsKeys.AUTH_REQUEST;
    }
}
