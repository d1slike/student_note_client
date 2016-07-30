package ru.disdev.network;

import ru.disdev.network.packets.ServerPacket;

/**
 * Created by DisDev on 22.07.2016.
 */
public class PacketWaiter {

    private final Object monitor = new Object();

    private volatile boolean waitAnswer;
    private volatile byte packetsKeyForWaiting;
    private volatile ServerPacket answer;

    private boolean successfullySentPackerToServer;

    public ServerPacket waitAnswer(byte packetId, long maxWaitTimeInSeconds) {
        synchronized (monitor) {
            packetsKeyForWaiting = packetId;
            if (!successfullySentPackerToServer || waitAnswer) //waitAnswer if already wait some packet
                return null;
            if (answer != null && packetsKeyForWaiting == answer.key())
                return answer;
            waitAnswer = true;
            final long timeInMillis = maxWaitTimeInSeconds * 1000L;
            long timeForInterruptWait = System.currentTimeMillis() + timeInMillis - 50;
            while (waitAnswer && System.currentTimeMillis() < timeForInterruptWait) {
                try {
                    monitor.wait(timeInMillis);
                } catch (InterruptedException ignored) {
                }
            }
            waitAnswer = false; //if time is over and answer was not came
            ServerPacket copy = answer;
            answer = null;
            return copy;
        }
    }

    boolean checkForWaitAndNotify(ServerPacket packet) {
        final boolean equal = packetsKeyForWaiting == packet.key();
        if (equal && waitAnswer) {
            synchronized (monitor) {
                answer = packet;
                waitAnswer = false;
                monitor.notifyAll();
            }
        }

        return equal;
    }

    public void setSuccessfullySentPackerToServer(boolean successfullySentPackerToServer) {
        this.successfullySentPackerToServer = successfullySentPackerToServer;
    }
}
