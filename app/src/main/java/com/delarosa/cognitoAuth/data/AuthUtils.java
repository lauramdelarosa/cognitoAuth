package com.delarosa.cognitoAuth.data;

import android.net.Uri;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;

public class AuthUtils {
    private static Auth auth;

    /**
     * Handles button press.
     *
     * @param signIn When {@code True} this performs sign-in.
     */
    public static void onButtonPress(boolean signIn) {
        Log.d(" -- ", "Button press: " + signIn);
        if (signIn) {
            auth.getSession();
        } else {
            auth.signOut();
        }
    }

    /**
     * guarda la sesion
     * @param builder
     */
    public static void buildAuth(Auth.Builder builder) {

        auth = builder.build();

    }

    /**
     * obtiene los tokens
     * @param data
     */
    public static void getTokens(Uri data) {
        auth.getTokens(data);
    }
}
