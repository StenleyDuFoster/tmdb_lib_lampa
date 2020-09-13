package lampa.test.tmdblib.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import lampa.test.tmdblib.R
import kotlin.random.Random

class NotificationBuilder(
    private val context: Context,
    private val title: String = "",
    private val subTitle: String = "",
    private val bigText: String = "",
    private val channelId: String = context.getString(R.string.chanelId),
    private val channelName: String = context.getString(R.string.chanelName)
) {

    fun createNotificationManager(): NotificationManager {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        return notificationManager
    }

    fun createBuiltNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(subTitle)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setSmallIcon(R.drawable.load_drawable)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(500, 500, 500, 500))
            .setAutoCancel(true)
    }

    fun createDefaultNotify() {
        val notifyManager = createNotificationManager()
        val buildNotify = createBuiltNotification()
        notifyManager.notify(Random.nextInt(), buildNotify.build())
    }

    fun createNotify(notifyManager: NotificationManager, buildNotify: NotificationCompat.Builder) {
        notifyManager.notify(Random.nextInt(), buildNotify.build())
    }
}