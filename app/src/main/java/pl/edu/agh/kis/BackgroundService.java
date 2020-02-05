package pl.edu.agh.kis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static java.lang.String.format;
import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;
import static pl.edu.agh.kis.StatisticKeeper.NOTIFICATION_CHANNEL_ID;

public class BackgroundService extends Service implements SensorEventListener {

    private final String TAG = getClass().getSimpleName();

    private HeartDroidManager heartDroidManager;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    final Runnable scheduled = () -> {
        Log.d(TAG, "Scheduler executed!");
        try {
            heartDroidManager.resolveNewState();
        } catch (NotInTheDomainException | AttributeNotRegisteredException | BuilderException e) {
            e.printStackTrace();
        }
    };

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "WALK_NOTIFICATION",
                android.app.NotificationManager.IMPORTANCE_DEFAULT);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StatisticKeeper.addSensorEvent(event);
        Log.d(TAG, format("Accuracy: %d, Timestamp %d, Values: %s", event.accuracy, event.timestamp, Arrays.toString(event.values)));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, format("onAccuracyChanged - Accuracy: %d", accuracy));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        heartDroidManager = setupHeartDroidManager(this);

        scheduler.scheduleAtFixedRate(scheduled,10, 10, TimeUnit.SECONDS);
        cleanupScheduler.scheduleAtFixedRate(StatisticKeeper::cleanup,1, 3, TimeUnit.HOURS);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        StatisticKeeper.mContext = this;
        createNotificationChannel();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
