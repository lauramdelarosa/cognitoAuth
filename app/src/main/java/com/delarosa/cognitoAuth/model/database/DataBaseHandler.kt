package com.delarosa.cognitoAuth.model.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.delarosa.cognitoAuth.model.Notification
import java.sql.SQLException

val DATABASE_NAME = "NotificationDB"
val TABLE_NAME = "NotificationTable"
val COL_TIME = "time"
val COL_DESCRIPTION = "description"
val COL_ID = "id"

class DataBaseHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, 1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TIME + " VARCHAR(100)," + COL_DESCRIPTION + " VARCHAR(100))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(notification: Notification) {
        val db = this.writableDatabase
        try {
            var cv = ContentValues()
            cv.put(COL_TIME, notification.time)
            cv.put(COL_DESCRIPTION, notification.description)
            db.insert(TABLE_NAME, null, cv)
        } catch (e: SQLException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

    }

    fun readData(): ArrayList<Notification> {
        var list: ArrayList<Notification> = ArrayList()
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val time = result.getString(1).toString()
                val description = result.getString(2).toString()
                var notification = Notification(time, description)
                list.add(notification)
            } while (result.moveToNext())
        }

        return list
    }
}