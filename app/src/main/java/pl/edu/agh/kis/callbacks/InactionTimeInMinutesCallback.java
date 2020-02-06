package pl.edu.agh.kis.callbacks;

import android.util.Log;

import heart.Callback;
import heart.WorkingMemory;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

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
        // TODO Implement
        return 10;
    }
    
}
