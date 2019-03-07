package com.delarosa.cognitoAuth.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout

import com.delarosa.cognitoAuth.model.AuthUtils
import com.delarosa.cognitoAuth.R
import com.delarosa.cognitoAuth.controller.NotificationAdapter
import com.delarosa.cognitoAuth.model.SharePreferences
import com.delarosa.cognitoAuth.model.database.DataBaseHandler
import com.delarosa.cognitoAuth.model.services.REFRESH_UI
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private var userButton: Button? = null
    private var sharePreferences: SharePreferences? = null
    private var TAG = "MyFirebase"
    lateinit var recyclerView: RecyclerView
    lateinit var db: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharePreferences = SharePreferences(this)
        val tokens = intent.extras
        var accessToken: String? = ""
        var IdToken: String? = ""
        if (tokens != null) {
            accessToken = tokens.getString(getString(R.string.app_access_token))
            IdToken = tokens.getString(getString(R.string.app_id_token))
        }
        Log.i("TOKEN", "Id Token" + IdToken!!)
        Log.i("TOKEN", "Access Token" + accessToken!!)


        userButton = findViewById(R.id.buttonSignout) as Button

        recyclerView = findViewById(R.id.notification_recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)

        db = DataBaseHandler(this)

        getFirebaseInstance()
        buildNotificationList()


        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                buildNotificationList()
            }

        }, IntentFilter(REFRESH_UI))

        userButton!!.setOnClickListener {
            sharePreferences!!.setBoolean(ISLOGGED, false)
            AuthUtils.onButtonPress(false)
        }

    }

    companion object {
        private val ISLOGGED = "isLogged"
    }

    private fun buildNotificationList() {
        val adapter = NotificationAdapter(db.readData())

        recyclerView.adapter = adapter
    }

    private fun getFirebaseInstance() {

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        if (intent.extras != null) {
            Log.d(TAG, "intent : " + intent.extras)
        }
    }
}
