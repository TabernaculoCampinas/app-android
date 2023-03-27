package br.org.tabernaculocampinas.ui.extension

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import br.org.tabernaculocampinas.R
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_DEFAULT
import com.google.android.exoplayer2.util.NotificationUtil.Importance

class NotificationExtension {
    companion object {
        fun createRadioNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                return

            val channel = NotificationChannel(
                context.getString(R.string.radio_notification_channel_id),
                "Tabernaculo Campinas",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Rádio em execução"
            channel.importance = NotificationManager.IMPORTANCE_LOW

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (!notificationManager.notificationChannels.contains(channel))
                notificationManager.createNotificationChannel(channel)
        }
    }
}