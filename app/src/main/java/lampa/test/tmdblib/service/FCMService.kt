package lampa.test.tmdblib.service

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import lampa.test.tmdblib.App
import lampa.test.tmdblib.util.notification.NotificationBuilder

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(this, remoteMessage)
    }

    private fun showNotification(context: Context, remoteMessage: RemoteMessage) {

        val notificationBuilder = NotificationBuilder(
            context,
            remoteMessage.notification?.title!!,
            remoteMessage.notification?.body!!
        )

        val notificationManager = notificationBuilder.createNotificationManager()
        val notificationBuild = notificationBuilder.createBuiltNotification()

        if (remoteMessage.notification?.imageUrl != null) {

            val imageBitmap = Glide.with(App.contextComponent)
                .asBitmap()
                .load(remoteMessage.notification?.imageUrl!!)
                .into(1000, 1000)
                .get()

            notificationBuild.setLargeIcon(imageBitmap)
        }
        notificationBuilder.createNotify(notificationManager,notificationBuild)
    }
}