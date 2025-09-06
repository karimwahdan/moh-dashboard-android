package com.kwdevs.hospitalsdashboard.app.notifications


import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.RemoteException
import android.util.Log
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Instance.Companion.context
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING

class NotificationHelper {
    private fun deleteChannelIfExists(channelId: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.getNotificationChannel(channelId)
        if (channel != null) {
            notificationManager.deleteNotificationChannel(channelId)
            Log.d("NotificationHelper", "Notification channel $channelId deleted.")
        } else Log.d("NotificationHelper", "Channel $channelId does not exist.")
    }

    private fun createNotificationChannel(channelId: String,channelName:String,
                                          sound: Uri, attributes: AudioAttributes) {
        deleteChannelIfExists(channelId)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.enableVibration(true)
        channel.vibrationPattern=longArrayOf(0, 1000, 500, 1000)
        channel.setSound(sound,attributes)
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try { notificationManager.createNotificationChannel(channel) } catch (e:RemoteException){ e.printStackTrace() }
    }


    fun showNotification(
        channelName:String= EMPTY_STRING,
        data:String){
        try{
            val vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            val notificationModel=NotificationConverter().notificationConverter(data)
            if(notificationModel!=null){
                FcmEventBus.icuOfferMessages.tryEmit(notificationModel)
                val message=notificationModel.message
                val soundPair: Pair<Uri, AudioAttributes> =
                    Pair(activeRequestSound().first, activeRequestSound().second)
                val channelId:String = EMPTY_STRING
                createNotificationChannel(channelId,channelName,soundPair.first,soundPair.second)

                val notification = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("يوجد تحديث على طلبك!!")
                    .setContentText(message)
                    .setVibrate(vibrationPattern)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notificationModel.message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                    ) { return }
                    notify(1, notification)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun activeRequestSound(): Pair<Uri, AudioAttributes> {
        val packageName = context.packageName
        val customSoundUri: Uri = Uri.parse("android.resource://${packageName}/raw/medlinc_sp_notification_sound01")
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        return Pair(customSoundUri,audioAttributes)
    }
}