package pl.edu.agh.kis;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.atomic.AtomicInteger;

import static pl.edu.agh.kis.StatisticKeeper.NOTIFICATION_CHANNEL_ID;
import static pl.edu.agh.kis.StatisticKeeper.mContext;

public class NotificationManager {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void showNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(StatisticKeeper.mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle("ES AML Project")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(counter.incrementAndGet(), builder.build());
    }
}
