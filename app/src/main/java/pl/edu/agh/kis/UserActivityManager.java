package pl.edu.agh.kis;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class UserActivityManager {

    private static final String TAG = UserActivityManager.class.getSimpleName();

    public static List<ActivityTransition> transitions;
    public static ActivityRecognitionClient activityRecognitionClient;
    public static Intent intent;
    public static PendingIntent transitionPendingIntent;
    public static ActivityTransitionResult activityTransitionResult;
    public static Context mContext;

    public static void registerHandler() {
        UserActivityManager.transitions = new ArrayList<>();
        UserActivityManager.transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        UserActivityManager.transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        UserActivityManager.transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        UserActivityManager.transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        ActivityTransitionRequest activityTransitionRequest = new ActivityTransitionRequest(UserActivityManager.transitions);

        Task<Void> task = activityRecognitionClient.requestActivityTransitionUpdates(activityTransitionRequest, transitionPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(mContext, "Transition update set up", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Transition update Failed to set up", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    public static void deregisterHandler() {
        Task<Void> task = activityRecognitionClient.removeActivityTransitionUpdates(transitionPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                transitionPendingIntent.cancel();
                Toast.makeText(mContext, "Remove Activity Transition Successfully", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Remove Activity Transition Failed", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    public static void handleTransition() {
        if (ActivityTransitionResult.hasResult(intent)) {
            activityTransitionResult = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : activityTransitionResult.getTransitionEvents()) {
                event.getActivityType();


                Toast.makeText(mContext, event.getTransitionType() + "-" + event.getActivityType(), Toast.LENGTH_LONG).show();
                //7 for walking and 8 for running
                Log.i(TAG, "Activity Type " + event.getActivityType());

                // 0 for enter, 1 for exit
                Log.i(TAG, "Transition Type " + event.getTransitionType());
            }
        }
    }
}
