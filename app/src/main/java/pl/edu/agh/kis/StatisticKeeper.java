package pl.edu.agh.kis;

import android.content.Context;
import android.hardware.SensorEvent;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticKeeper {

    private static final String TAG = StatisticKeeper.class.getSimpleName();

    private static ConcurrentHashMap<LocalDateTime, Float> stepsData;
    public static Context mContext;
    public static final String NOTIFICATION_CHANNEL_ID = "WALK_NOTIFY";

    public static void cleanup() {
        Log.d(TAG, "Cleanup scheduler executed!");
        if (stepsData == null) {
            stepsData = new ConcurrentHashMap<>();
        }
        ConcurrentHashMap<LocalDateTime, Float> cleanedStepsData = new ConcurrentHashMap<>();
        for (Map.Entry e : stepsData.entrySet()) {
            LocalDate eLocalDate = ((LocalDateTime)e.getKey()).toLocalDate();
            if (eLocalDate.isAfter(LocalDate.now().minusDays(2))) {
                cleanedStepsData.put((LocalDateTime) e.getKey(), (Float) e.getValue());
            }
        }
        stepsData = cleanedStepsData;
    }

    public static void addSensorEvent(SensorEvent event) {
        if (stepsData == null) {
            stepsData = new ConcurrentHashMap<>();
        }
        stepsData.put(LocalDateTime.now(), event.values[0]);
    }

    public static ConcurrentHashMap<LocalDateTime, Float> getStepsData() {
        return stepsData;
    }
}
