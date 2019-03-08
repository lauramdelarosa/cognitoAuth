package com.delarosa.cognitoAuth.model.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.delarosa.cognitoAuth.R
import com.delarosa.cognitoAuth.model.Notification
import com.delarosa.cognitoAuth.model.database.DataBaseHandler
import com.delarosa.cognitoAuth.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

const val REFRESH_UI = "RefreshUi"
const val TAG = "MyFirebase"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val CHANNEL_ID = "om.delarosa.cognitoAuth"

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: " + token)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        /*  sendRegistrationToServer(token);*/
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, " onMessageReceived ")
        if (remoteMessage?.data!!.isNotEmpty()) {
            Log.d(TAG, " Data : " + remoteMessage.data.toString())

        }

        if (remoteMessage.notification != null) {

            var tittle = remoteMessage.notification!!.title.toString()
            val description = remoteMessage.notification!!.body.toString()
            buildNotification(tittle, description)
            saveNotificationDatabase(description)
            refreshUI()
        }

    }


    private fun refreshUI() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.sendBroadcast(Intent(REFRESH_UI))
    }

    private fun saveNotificationDatabase(description: String) {
        val simpleDateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val time = simpleDateFormat.format(Date())
        var db = DataBaseHandler(this)
        db.insertData(Notification(time, description))
    }

    private fun buildNotification(title: String, description: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fromNotification", true)
        val contentIntent = PendingIntent.getActivity(
                this,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        val notiStyle = NotificationCompat.BigPictureStyle()
        notiStyle.setSummaryText(description)
        val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val SUMMARY_ID = 0
        val channelName = "push notification"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            notificationBuilder
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(description))
                    .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
                    .setSound(defaultSoundUri)
                    .setGroup(CHANNEL_ID)
                    .setContentIntent(contentIntent)
        } else {
            notificationBuilder
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(description))
                    .setSound(defaultSoundUri)
                    .setGroup(CHANNEL_ID)
                    .setContentIntent(contentIntent)
        }

        val summaryNotification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(channelName)
                .setSmallIcon(R.drawable.notification_icon)
                .setGroup(CHANNEL_ID)
                .setGroupSummary(true)
                .build()

        NotificationManagerCompat.from(this).apply {
            notify(SUMMARY_ID, summaryNotification)

        }
        notificationManager!!.notify(
                System.currentTimeMillis().toInt(),
                notificationBuilder.build()
        )
    }


}