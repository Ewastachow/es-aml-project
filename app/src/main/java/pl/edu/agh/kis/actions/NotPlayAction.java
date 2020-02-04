package pl.edu.agh.kis.actions;

import android.util.Log;

import heart.Action;
import heart.State;

public class NotPlayAction implements Action {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(State state) {
        Log.i(TAG, "IMPORTANT! NotPlayAction");
    }

}
