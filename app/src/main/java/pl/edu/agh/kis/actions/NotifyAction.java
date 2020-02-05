package pl.edu.agh.kis.actions;

import android.util.Log;

import heart.Action;
import heart.State;
import pl.edu.agh.kis.NotificationManager;

public class NotifyAction implements Action {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(State state) {
        Log.i(TAG, "IMPORTANT! NotifyAction");
        NotificationManager.showNotification("Notify Action!!!");
    }

}