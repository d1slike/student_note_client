package ru.disdev.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import ru.disdev.utils.ConcurrencyUtils;
import ru.disdev.UserSettings;
import ru.disdev.collbacks.AuthResultHandler;
import ru.disdev.collbacks.Consumer;
import ru.disdev.model.UserInfo;
import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.components.InboundPacketsKeys;
import ru.disdev.network.packets.in.AuthResponse;
import ru.disdev.network.packets.out.AuthRequest;

/**
 * Created by DisDev on 23.07.2016.
 */
public class AuthController {

    public static final int AUTH_REQUEST = 665;
    public static final String SILENT_SIGN_IN_EXTRA_ID = "silentSignIn";
    public static final String MAKE_LOGOUT_EXTRA_ID = "makeLogout";

    private static final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("608114583618-poi30d24lmj9v5jvjjedee5ce5ck7kir.apps.googleusercontent.com")
            .requestEmail()
            .build();

    private static final GoogleApiClient.OnConnectionFailedListener LISTENER = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            //ignore
        }
    };

    public static GoogleApiClient makeApiClient(FragmentActivity activity) {
        return new GoogleApiClient
                .Builder(activity)
                .enableAutoManage(activity, LISTENER)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public static Scope[] getScopes() {
        return gso.getScopeArray();
    }

    public static void silentSignIn(GoogleApiClient client, final AuthResultHandler handler) {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(client);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSighInResult(result, handler);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSighInResult(googleSignInResult, handler);
                }
            });
        }
    }

    public static void handleSighInResult(final GoogleSignInResult result, final AuthResultHandler handler) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            ConcurrencyUtils.sendPacketToServerAndHandleAnswer(new AuthRequest(acct.getIdToken()),
                    InboundPacketsKeys.AUTH_RESPONSE,
                    5,
                    new Consumer<ServerPacket>() {
                        @Override
                        public void consume(ServerPacket packet) {
                            AuthResponse response = (AuthResponse) packet;
                            boolean success = response != null && response.isSuccess();
                            if (success) {
                                GoogleSignInAccount account = result.getSignInAccount();
                                UserInfo info = response.makeUser(account.getDisplayName(), account.getEmail());
                                UserSettings.setCurrentUser(info);
                                handler.onSuccess();
                            } else
                                handler.onError(GoogleSignInStatusCodes.NETWORK_ERROR);
                        }
                    });
        } else {
            handler.onError(result.getStatus().getStatusCode());
        }
    }

    public static void signOut(final GoogleApiClient apiClient,final ResultCallback<Status> statusResultCallback) {
        try {
            apiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(statusResultCallback);
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            });
            apiClient.reconnect();
        } catch (Exception e) {
            Log.e("error", "signout", e);
        }

    }

}
