package pl.edu.agh.kis;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.Source;
import heart.parser.hmr.runtime.SourceString;
import heart.xtt.XTTModel;

public class HeartDroidUtil {

    private final XTTModel model;
    private final String[] tablesNames;

    public HeartDroidUtil(Context context, String hmrPath, String[] tablesNames) throws ParsingSyntaxException, IOException, ModelBuildingException {
        this.model = createXttModel(context, hmrPath);
        this.tablesNames = tablesNames;
    }

    private static XTTModel createXttModel(Context context, String hmrPath) throws ModelBuildingException, ParsingSyntaxException, IOException {
        InputStream inputStream = context.getAssets().open(hmrPath);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String rules = result.toString("UTF-8");
        inputStream.close();
        Source source = new SourceString(rules);
        HMRParser parser = new HMRParser();
        parser.parse(source);
        return parser.getModel();
    }

    public State resolveNewState(State state) throws NotInTheDomainException, BuilderException, AttributeNotRegisteredException {
        HeaRT.dataDrivenInference(model,
                tablesNames,
                new Configuration.Builder()
                        .setInitialState(state)
                        .build());
        return HeaRT.getWm().getCurrentState(model);
    }
}
