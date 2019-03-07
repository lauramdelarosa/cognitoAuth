package com.delarosa.cognitoAuth.model.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

const val TAG = "MyFirebase"

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        //Send this token to server for later use
    }
}

