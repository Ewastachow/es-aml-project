package pl.edu.agh.kis.callbacks;

import android.util.Log;

import java.time.LocalDate;
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

public class TodayStepsCallback implements Callback {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(Attribute attribute, WorkingMemory workingMemory) {
        Log.i(TAG, "Executing TodayStepsCallback for " + attribute.getName());
        try {
            workingMemory.setAttributeValue(attribute, new SimpleNumeric(resolveTodaySteps()), false);
        } catch (AttributeNotRegisteredException | NotInTheDomainException e) {
            Log.i(TAG, "Error occurred: ", e);
        }
    }

    private double resolveTodaySteps() {
        List<Map.Entry<LocalDateTime, Float>> todays = StatisticKeeper.getStepsData().entrySet().stream()
                .filter(entry -> entry.getKey().toLocalDate().isEqual(LocalDate.now()))
                .sorted((ldt1, ldt2) -> ldt1.getKey().compareTo(ldt2.getKey()))
                .collect(Collectors.toList());
        double end = todays.get(todays.size() - 1).getValue().doubleValue();
        double begin = todays.get(0).getValue().doubleValue();
        return end - begin;
    }

}
