package pl.edu.agh.kis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;
import static pl.edu.agh.kis.UserActivityManager.activityRecognitionClient;
import static pl.edu.agh.kis.UserActivityManager.transitionPendingIntent;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private HeartDroidManager heartDroidManager;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartDroidManager = setupHeartDroidManager(this);

        scheduler.scheduleAtFixedRate(scheduled,2, 5, TimeUnit.SECONDS);

        UserActivityManager.mContext = this;
        activityRecognitionClient = ActivityRecognition.getClient(UserActivityManager.mContext);
        UserActivityManager.intent = new Intent(this, TransitionIntentService.class);
        transitionPendingIntent = PendingIntent.getService(this, 100, UserActivityManager.intent, PendingIntent.FLAG_UPDATE_CURRENT);
        UserActivityManager.registerHandler();
    }

    final Runnable scheduled = new Runnable() {
        public void run() {
            Log.d(TAG, "Scheduler executed!");
            try {
                UserActivityManager.handleTransition();
                heartDroidManager.resolveNewState();
            } catch (NotInTheDomainException | AttributeNotRegisteredException | BuilderException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserActivityManager.deregisterHandler();
    }

}
