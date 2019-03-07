package com.delarosa.cognitoAuth.controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.delarosa.cognitoAuth.R
import com.delarosa.cognitoAuth.model.Notification

class NotificationAdapter(private val notificationList: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val notification: Notification = notificationList[position]
        holder?.textViewTime?.text = notification.time
        holder?.textViewDescription?.text = notification.description
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup?.context).inflate(R.layout.message_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTime = itemView.findViewById(R.id.time_notification) as TextView
        val textViewDescription = itemView.findViewById(R.id.description_notification) as TextView
    }

}