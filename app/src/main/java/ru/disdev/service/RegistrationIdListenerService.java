package ru.disdev.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Dislike on 19.07.2016.
 */
public class RegistrationIdListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, IDRegistrationService.class);
        startService(intent);
    }
}
