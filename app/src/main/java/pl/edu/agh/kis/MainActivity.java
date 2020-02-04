package pl.edu.agh.kis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;

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

}
