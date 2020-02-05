package pl.edu.agh.kis.callbacks;

import android.util.Log;

import heart.Callback;
import heart.WorkingMemory;
import heart.alsvfd.SimpleSymbolic;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

public class UserActivityCallback implements Callback {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute(Attribute attribute, WorkingMemory workingMemory) {

        Log.i(TAG, "Executing HumidityCallback for " + attribute.getName());
        try {
            workingMemory.setAttributeValue(attribute, new SimpleSymbolic("high"), false);
        } catch (AttributeNotRegisteredException | NotInTheDomainException e) {
            Log.i(TAG, "Error occurred: ", e);
        }

    }
    
}