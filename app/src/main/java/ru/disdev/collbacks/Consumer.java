package ru.disdev.collbacks;

import ru.disdev.network.packets.ServerPacket;

/**
 * Created by DisDev on 23.07.2016.
 */
public interface Consumer<T> {
    void consume(T t);
}
