package pl.edu.agh.kis.actions;

import android.util.Log;

import heart.Action;
import heart.State;
import pl.edu.agh.kis.NotificationManager;

public class NotifyTooLongInactionTimeAction implements Action {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(State state) {
        Log.i(TAG, "IMPORTANT! NotifyTooLongInactionTimeAction");
        NotificationManager.showNotification("Too long inaction time! Go walk.");
    }

}