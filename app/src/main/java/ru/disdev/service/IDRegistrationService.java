package ru.disdev.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ru.disdev.R;
import ru.disdev.UserSettings;
import ru.disdev.network.ConnectionHolder;
import ru.disdev.network.packets.out.GCMRegistration;

/**
 * Created by Dislike on 19.07.2016.
 */
public class IDRegistrationService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IDRegistrationService(String name) {
        super(name);
    }


    public IDRegistrationService() {
        super("RegIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            ConnectionHolder.getInstance().connect().sendPacketToServer(
                    new GCMRegistration(token, UserSettings.getCurrentUserId()));
        } catch (IOException ignored) {
        }
    }
}
