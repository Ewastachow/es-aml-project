package pl.edu.agh.kis.actions;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.atomic.AtomicInteger;

import heart.Action;
import heart.State;
import pl.edu.agh.kis.R;
import pl.edu.agh.kis.StatisticKeeper;

import static pl.edu.agh.kis.StatisticKeeper.NOTIFICATION_CHANNEL_ID;
import static pl.edu.agh.kis.StatisticKeeper.mContext;

public class NotifyAction implements Action {

    private final String TAG = getClass().getSimpleName();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void execute(State state) {
        Log.i(TAG, "IMPORTANT! NotifyAction");
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(StatisticKeeper.mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle("LAMA")
                .setContentText("ALPAKA")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(counter.incrementAndGet(), builder.build());
    }

}