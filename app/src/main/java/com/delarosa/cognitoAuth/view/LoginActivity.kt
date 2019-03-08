package com.delarosa.cognitoAuth.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button

import com.amazonaws.mobileconnectors.cognitoauth.Auth
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession
import com.amazonaws.mobileconnectors.cognitoauth.handlers.AuthHandler
import com.delarosa.cognitoAuth.model.AuthUtils
import com.delarosa.cognitoAuth.R
import com.delarosa.cognitoAuth.model.SharePreferences

class LoginActivity : FragmentActivity() {

    private var appRedirect: Uri? = null
    private var userButton: Button? = null
    private var sharePreferences: SharePreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initCognito()
        sharePreferences = SharePreferences(this)
        if (sharePreferences!!.getBoolean(ISLOGGED)) {
            intentMainActivity(null)
        } else {
            setNewUserFragment()
        }

    }

    override fun onResume() {
        super.onResume()
        val activityIntent = intent
        //  -- Call Auth.getTokens() to get Cognito JWT --
        if (activityIntent.data != null && appRedirect!!.host == activityIntent.data!!.host) {
            AuthUtils.getTokens(activityIntent.data)
        }
    }

    /**
     * Sets new user fragment on the screen.
     */
    private fun setNewUserFragment() {
        userButton = findViewById<View>(R.id.buttonSignin) as Button
        userButton!!.setOnClickListener { AuthUtils.onButtonPress(true) }
    }

    /**
     * Sets auth user fragment.
     *
     * @param session [AuthUserSession] containing tokens for a user.
     */
    private fun intentMainActivity(session: AuthUserSession?) {
        sharePreferences!!.setBoolean(ISLOGGED, true)
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        if (session != null) {
            intentToMainActivity.putExtra(getString(R.string.app_access_token), session.accessToken.jwtToken)
            intentToMainActivity.putExtra(getString(R.string.app_id_token), session.idToken.jwtToken)
        }

        startActivity(intentToMainActivity)
    }


    /**
     * Setup authentication with Cognito.
     */
    internal fun initCognito() {
        //  -- Create an instance of Auth --
        val builder = Auth.Builder().setAppClientId(getString(R.string.cognito_client_id))
                .setAppClientSecret(getString(R.string.cognito_client_secret))
                .setAppCognitoWebDomain(getString(R.string.cognito_web_domain))
                .setApplicationContext(applicationContext)
                .setAuthHandler(callback())
                .setSignInRedirect(getString(R.string.app_redirect))
                .setSignOutRedirect(getString(R.string.app_redirect))
        AuthUtils.buildAuth(builder)
        appRedirect = Uri.parse(getString(R.string.app_redirect))
    }


    /**
     * Callback handler for Amazon Cognito.
     */
    internal inner class callback : AuthHandler {

        override fun onSuccess(authUserSession: AuthUserSession) {
            // Show tokens for the authenticated user
            intentMainActivity(authUserSession)
        }

        override fun onSignout() {
            // Back to new user screen.
            setNewUserFragment()
        }

        override fun onFailure(e: Exception) {
            Log.e(TAG, "Failed to auth", e)
        }
    }

    companion object {
        private val TAG = "CognitoAuthDemo"
        private val ISLOGGED = "isLogged"
    }
}
