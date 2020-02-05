package pl.edu.agh.kis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = getClass().getSimpleName();

    private HeartDroidManager heartDroidManager;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float mStepsSinceReboot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartDroidManager = setupHeartDroidManager(this);

        scheduler.scheduleAtFixedRate(scheduled,2, 5, TimeUnit.SECONDS);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    final Runnable scheduled = new Runnable() {
        public void run() {
            Log.d(TAG, "Scheduler executed!");
            try {
                heartDroidManager.resolveNewState();
            } catch (NotInTheDomainException | AttributeNotRegisteredException | BuilderException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        mStepsSinceReboot = event.values[0];
        Log.i(TAG, "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "");
    }
}
