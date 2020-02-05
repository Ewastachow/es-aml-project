package pl.edu.agh.kis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static java.lang.String.format;
import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = getClass().getSimpleName();

    private HeartDroidManager heartDroidManager;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartDroidManager = setupHeartDroidManager(this);

        scheduler.scheduleAtFixedRate(scheduled,10, 10, TimeUnit.SECONDS);
        cleanupScheduler.scheduleAtFixedRate(StatisticKeeper::cleanup,1, 3, TimeUnit.HOURS);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    final Runnable scheduled = () -> {
        Log.d(TAG, "Scheduler executed!");
        try {
            heartDroidManager.resolveNewState();
        } catch (NotInTheDomainException | AttributeNotRegisteredException | BuilderException e) {
            e.printStackTrace();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
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
}
