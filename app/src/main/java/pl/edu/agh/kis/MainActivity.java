package pl.edu.agh.kis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

import static pl.edu.agh.kis.HeartDroidManager.setupHeartDroidManager;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private HeartDroidManager heartDroidManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartDroidManager = setupHeartDroidManager(this);

        try {
            heartDroidManager.resolveNewState();
        } catch (NotInTheDomainException | AttributeNotRegisteredException | BuilderException e) {
            e.printStackTrace();
        }
    }



}
