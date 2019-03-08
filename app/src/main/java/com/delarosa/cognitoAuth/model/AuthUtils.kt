package com.delarosa.cognitoAuth.model

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log

import com.amazonaws.mobileconnectors.cognitoauth.Auth

object AuthUtils {
     @SuppressLint("StaticFieldLeak")
     var auth: Auth? = null

    /**
     * guarda la sesion
     * @param builder
     */
    fun buildAuth(builder: Auth.Builder) {
        auth = builder.build()
    }

    /**
     * gestiona el boton de inicio o cierre de sesion
     * @param signIn When `True` this performs sign-in.
     */
    fun onButtonPress(signIn: Boolean) {
        Log.d(" -- ", "Button press: $signIn")
        if (signIn) {
            auth!!.getSession()
        } else {
            auth!!.signOut()
        }
    }

    /**
     * obtiene los tokens
     * @param data
     */
    fun getTokens(data: Uri) {
        auth!!.getTokens(data)
    }

}
