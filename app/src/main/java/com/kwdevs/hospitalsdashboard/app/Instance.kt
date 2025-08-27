package com.kwdevs.hospitalsdashboard.app

import android.Manifest
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.kwdevs.hospitalsdashboard.models.Update
import com.orhanobut.hawk.Hawk
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.connection.ConnectionEventListener

class Instance: Application() {
    lateinit var pusher: Pusher
    companion object{
        private lateinit var instance: Application
        val context: Context get() = instance.applicationContext
        val pusherInstance: Pusher get() = (instance as Instance).pusher
        private fun getAppVersionInfo(): Long {
            return try {
                val context = Instance.context
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // For API level 33 and above
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    // For API level below 33
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }

                // Retrieve version code and version name
                val versionCode = packageInfo.longVersionCode
                packageInfo.versionName
                versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                -1
            }
        }

        val versionCode: Long by lazy { getAppVersionInfo() }

        fun checkUpdates(update: Update){
            val packageName = context.packageName
            val marketUri: Uri = Uri.parse("market://details?id=$packageName")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            marketIntent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            if(versionCode<update.vCode && update.requiresUpdate==1){
                try {
                    context.startActivity(marketIntent, null)
                }
                catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                    val uri =
                        "http://play.google.com/store/apps/details?id=$packageName"
                    val flag = Intent.ACTION_VIEW
                    val i = Intent(flag, Uri.parse(uri))
                    context.startActivity( i, null)
                }
            }
        }

    }
    private var hasNotificationPermission: Boolean = false
        private set
    override fun onCreate() {
        super.onCreate()
        instance =this
        Hawk.init(this).build()
        checkNotificationPermission()
        val options = PusherOptions().apply { setCluster("eu"); }
        pusher = Pusher("3d8e58cfacb7861b1780", options)

        connectPusher()
    }

    private fun connectPusher() {
        try {
            pusher.connect(object : ConnectionEventListener {
                override fun onConnectionStateChange(change: ConnectionStateChange) {
                    Log.i(
                        "Pusher Connecting",
                        "State changed from ${change.previousState} to ${change.currentState}"
                    )
                }
                override fun onError(message: String, code: String?, e: Exception?) {
                    try{
                        Log.i("Pusher Connecting",
                            "There was a problem connecting!, message ($message), exception(${e?.message})")
                    }
                    catch (e:Exception){ e.printStackTrace() }
                }
            }, ConnectionState.CONNECTED
            )

        }
        catch (e: Exception) { e.printStackTrace() }
    }
    private fun checkNotificationPermission(): Boolean {
        hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        return hasNotificationPermission
    }
    //fun updateNotificationPermissionStatus(isGranted: Boolean) {hasNotificationPermission = isGranted}

}