package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import com.google.common.base.Charsets;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.parser.hml.HMLParser;
import heart.xtt.XTTModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class RulesProvider {
    private static final String POI_RULES_FILENAME = "poi_rules.hml";

    private final Context context;

    public XTTModel loadXttModel() {
        final InputStream xttModelInputStream;

        File poiRulesFile = new File(context.getFilesDir(), POI_RULES_FILENAME);
        if (poiRulesFile.exists()) {
            try {
                xttModelInputStream = new FileInputStream(poiRulesFile);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
        } else {
            xttModelInputStream = context.getResources().openRawResource(R.raw.basic_rules);
        }

        try {
            return HMLParser.parseHML(xttModelInputStream);
        } catch (BuilderException | NotInTheDomainException | RangeFormatException e) {
            log.error("Could not load XttModel.", e);
            return null;
        } finally {
            try {
                xttModelInputStream.close();
            } catch (IOException e) {
                log.error("IOException when closing XttModel stream.", e);
            }
        }
    }

    public void saveXttModel(String xttModel) throws IOException {
        FileOutputStream oos = context.openFileOutput(POI_RULES_FILENAME, Context.MODE_PRIVATE);
        oos.write(xttModel.getBytes(Charsets.UTF_8));
        oos.close();
    }
}
