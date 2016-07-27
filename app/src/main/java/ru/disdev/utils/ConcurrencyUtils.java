package ru.disdev.utils;

import android.os.AsyncTask;

import com.fernandocejas.arrow.optional.Optional;

import java.io.Serializable;

import ru.disdev.collbacks.Consumer;
import ru.disdev.collbacks.Producer;
import ru.disdev.network.ConnectionHolder;
import ru.disdev.network.packets.ClientPacket;
import ru.disdev.network.packets.ServerPacket;

/**
 * Created by DisDev on 22.07.2016.
 */
public class ConcurrencyUtils {


    public static void sendPacketToServerAndHandleAnswer(ClientPacket packetToSend,
                                                         byte answerId,
                                                         long maxSecondsToWait,
                                                         final Consumer<ServerPacket> packetConsumer) {
        final AsyncTask<RequestInfo, Void, ServerPacket> packetAsyncTask = new AsyncTask<RequestInfo, Void, ServerPacket>() {
            @Override
            @SuppressWarnings("uncheked")
            protected ServerPacket doInBackground(RequestInfo... params) {
                RequestInfo info = params[0];
                final ClientPacket packetToSend = info.packetToSend;
                final byte answerId = info.packetAnswerId;
                final long maxSecondsToWait = info.maxTimeToWait;

                ServerPacket packet = ConnectionHolder.getInstance()
                        .connect()
                        .sendPacketToServerAndWaitAnswer(packetToSend)
                        .waitAnswer(answerId, maxSecondsToWait);

                return packet;
            }

            @Override
            protected void onPostExecute(ServerPacket packet) {
                super.onPostExecute(packet);
                packetConsumer.consume(packet);
            }
        };
        RequestInfo info = new RequestInfo(packetToSend, answerId, maxSecondsToWait);
        packetAsyncTask.executeOnExecutor(ConnectionHolder.getInstance().getExecutor(), info);
    }

    public static <T> AsyncTask<Void, Void, T> executeOnBackground(final Producer<T> callable, final Consumer<T> consumer) {
        final AsyncTask<Void, Void, T> asyncTask = new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                return callable.produce();
            }

            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                consumer.consume(t);
            }
        }.executeOnExecutor(ConnectionHolder.getInstance().getExecutor());

        return asyncTask;
    }

    private static class RequestInfo {
        final ClientPacket packetToSend;
        final byte packetAnswerId;
        final long maxTimeToWait;

        private RequestInfo(ClientPacket packetToSend, byte packetAnswerId, long maxTimeToWait) {
            this.packetToSend = packetToSend;
            this.packetAnswerId = packetAnswerId;
            this.maxTimeToWait = maxTimeToWait;
        }
    }
}
