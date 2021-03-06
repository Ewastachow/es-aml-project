package pl.edu.agh.kis.actions;

import android.util.Log;

import heart.Action;
import heart.State;
import pl.edu.agh.kis.NotificationManager;

public class NotifyTooFewStepsInLastHourAction implements Action {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(State state) {
        Log.i(TAG, "IMPORTANT! NotifyTooFewStepsInLastHourAction");
        NotificationManager.showNotification("Too few steps in last hour! Go walk.");
    }

}