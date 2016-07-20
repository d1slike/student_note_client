package ru.disdev.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import ru.disdev.R;
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

            // TODO: Implement this method to send any registration to your app's servers.
            ConnectionHolder.getInstance().connect().sendPacketToServer(new GCMRegistration(token, 1));
        } catch (Exception e) {
        }
    }
}
