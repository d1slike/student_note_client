package ru.disdev.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import ru.disdev.R;
import ru.disdev.ServiceStarter;
import ru.disdev.UserSettings;
import ru.disdev.collbacks.AuthResultHandler;
import ru.disdev.controllers.AuthController;
import ru.disdev.network.ConnectionHolder;
import ru.disdev.utils.ConcurrencyUtils;
import ru.disdev.collbacks.Consumer;
import ru.disdev.collbacks.Producer;

/**
 * Created by DisDev on 23.07.2016.
 */
public class AuthActivity extends FragmentActivity {
    private SignInButton signInButton;
    private TextView authInfo;
    private ProgressBar waitAuthProgressBar;

    private AuthResultHandler manualSignInHandler;
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_auth);
        authInfo = (TextView) findViewById(R.id.authInfo);
        waitAuthProgressBar = (ProgressBar) findViewById(R.id.authWaitProgressBar);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setScopes(AuthController.getScopes());
        client = AuthController.makeApiClient(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(false);
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
                startActivityForResult(intent, AuthController.AUTH_REQUEST);
            }
        });
        manualSignInHandler = new AuthResultHandler() {
            @Override
            public void onSuccess() {
                onSuccessAuth();
            }

            @Override
            public void onError(int errorCode) {
                updateUI(true);
            }
        };

        if (getIntent().getBooleanExtra(AuthController.MAKE_LOGOUT_EXTRA_ID, false)) {
            AuthController.signOut(client, new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    updateUI(true);
                }
            });
        }

        boolean silentSignIn = getIntent().getBooleanExtra(AuthController.SILENT_SIGN_IN_EXTRA_ID, true);
        if (silentSignIn) {
            updateUI(false);
            ConcurrencyUtils.executeOnBackground(new Producer<Void>() {
                @Override
                public Void produce() {
                    ConnectionHolder.getInstance().connect();
                    return null;
                }
            }, new Consumer<Void>() {
                @Override
                public void consume(Void aVoid) {
                    AuthController.silentSignIn(client, new AuthResultHandler() {
                        @Override
                        public void onSuccess() {
                            onSuccessAuth();
                        }

                        @Override
                        public void onError(int errorCode) {
                            if (errorCode == GoogleSignInStatusCodes.NETWORK_ERROR)
                                authInfo.setText("Ошибка установки соединения с сервером");
                            updateUI(true);
                        }
                    });
                }
            });
        } else
            updateUI(true);

    }

    private void updateUI(boolean needAuth) {
        if (needAuth) {
            signInButton.setVisibility(View.VISIBLE);
            authInfo.setVisibility(View.VISIBLE);
            waitAuthProgressBar.setVisibility(View.INVISIBLE);
        } else {
            signInButton.setVisibility(View.INVISIBLE);
            authInfo.setVisibility(View.INVISIBLE);
            waitAuthProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AuthController.AUTH_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            AuthController.handleSighInResult(result, manualSignInHandler);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manualSignInHandler = null;
        client = null;
    }

    private void onSuccessAuth() {
        startActivity(new Intent(this, UserSettings.hasGroup() ? MainActivity.class : GroupManageActivity.class));
        //ServiceStarter.startGCMService(this);
        finish();
    }


}
