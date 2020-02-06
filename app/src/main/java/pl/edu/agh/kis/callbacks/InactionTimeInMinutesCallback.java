package pl.edu.agh.kis.callbacks;

import android.util.Log;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import heart.Callback;
import heart.WorkingMemory;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;
import pl.edu.agh.kis.StatisticKeeper;

public class InactionTimeInMinutesCallback implements Callback {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(Attribute attribute, WorkingMemory workingMemory) {
        Log.i(TAG, "Executing InactionTimeInMinutesCallback for " + attribute.getName());
        try {
            workingMemory.setAttributeValue(attribute, new SimpleNumeric(resolveInactionTimeInMinutes()), false);
        } catch (AttributeNotRegisteredException | NotInTheDomainException e) {
            Log.i(TAG, "Error occurred: ", e);
        }
    }

    private double resolveInactionTimeInMinutes() {
        List<Map.Entry<LocalDateTime, Float>> collect = StatisticKeeper.getStepsData().entrySet().stream()
                .sorted((ldt1, ldt2) -> ldt2.getKey().compareTo(ldt1.getKey()))
                .collect(Collectors.toList());
        LocalDateTime newestTime = collect.get(0).getKey();
        Float newestSteps = collect.get(0).getValue();
        Duration inaction = Duration.ofHours(24);
        for (Map.Entry<LocalDateTime, Float> entry: collect) {
            LocalDateTime time = entry.getKey();
            Float steps = entry.getValue();
            boolean isStillInactive = isStillInactive(newestTime, time, newestSteps, steps);
            if (isStillInactive) {
                inaction = Duration.between(time, newestTime);
            } else if (newestTime != time) {
                return inaction.toMinutes();
            }
        }
        return inaction.toMinutes();
    }

    private boolean isStillInactive(LocalDateTime newestTime, LocalDateTime time, Float newestSteps, Float steps) {
        if (newestTime == time) {
            return true;
        }
        double duration = Duration.between(time, newestTime).toMinutes();
        float stepsDiff = newestSteps - steps;
        return stepsDiff < 20 || stepsDiff / duration < 30;
    }
    
}
