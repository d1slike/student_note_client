package ru.disdev;

import android.content.Context;
import android.content.Intent;

import ru.disdev.service.IDRegistrationService;

/**
 * Created by DisDev on 22.07.2016.
 */
public class ServiceStarter {
    private static boolean GCMServiceIsUp;

    public static void startGCMService(Context context) {
        if (GCMServiceIsUp)
            return;
        context.startService(new Intent(context, IDRegistrationService.class));
        GCMServiceIsUp = true;
    }
}
