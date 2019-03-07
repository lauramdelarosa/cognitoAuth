package com.delarosa.cognitoAuth;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession;
import com.amazonaws.mobileconnectors.cognitoauth.handlers.AuthHandler;

public class LoginActivity extends FragmentActivity {
    private static final String TAG = "CognitoAuthDemo";
    private AlertDialog userDialog;
    private Uri appRedirect;
    private Button userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initCognito();
        setNewUserFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent activityIntent = getIntent();
        //  -- Call Auth.getTokens() to get Cognito JWT --
        if (activityIntent.getData() != null && appRedirect.getHost().equals(activityIntent.getData().getHost())) {
            AuthUtils.getTokens(activityIntent.getData());
        }
    }

    /**
     * Sets new user fragment on the screen.
     */
    private void setNewUserFragment() {
        userButton = (Button) findViewById(R.id.buttonSignin);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
    }

    /**
     * Sets auth user fragment.
     *
     * @param session {@link AuthUserSession} containing tokens for a user.
     */
    private void setAuthUserFragment(AuthUserSession session) {

        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.putExtra(getString(R.string.app_access_token), session.getAccessToken().getJWTToken());
        intentToMainActivity.putExtra(getString(R.string.app_id_token), session.getIdToken().getJWTToken());
        startActivity(intentToMainActivity);


    }

    public void onButtonPressed() {
        AuthUtils.onButtonPress(true);
    }


    /*@Override
    public void showPopup(String title, String content) {
        showDialogMessage(title, content);
    }
*/

    /**
     * Setup authentication with Cognito.
     */
    void initCognito() {
        //  -- Create an instance of Auth --
        Auth.Builder builder = new Auth.Builder().setAppClientId(getString(R.string.cognito_client_id))
                .setAppClientSecret(getString(R.string.cognito_client_secret))
                .setAppCognitoWebDomain(getString(R.string.cognito_web_domain))
                .setApplicationContext(getApplicationContext())
                .setAuthHandler(new callback())
                .setSignInRedirect(getString(R.string.app_redirect))
                .setSignOutRedirect(getString(R.string.app_redirect));
        AuthUtils.buildAuth(builder);
        appRedirect = Uri.parse(getString(R.string.app_redirect));
    }

    /**
     * Show an popup dialog.
     *
     * @param title
     * @param body
     */
    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();

                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG, "Dialog failure", e);
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }


    /**
     * Callback handler for Amazon Cognito.
     */
    class callback implements AuthHandler {

        @Override
        public void onSuccess(AuthUserSession authUserSession) {
            // Show tokens for the authenticated user
            setAuthUserFragment(authUserSession);
        }

        @Override
        public void onSignout() {
            // Back to new user screen.
            setNewUserFragment();
        }

        @Override
        public void onFailure(Exception e) {
            Log.e(TAG, "Failed to auth", e);
        }
    }
}