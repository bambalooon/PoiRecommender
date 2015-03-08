package pl.edu.agh.eis.poirecommender.interests;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Krzysztof Balon on 2014-11-05.
 */
public class InterestPreferences {
    private static final Gson GSON_SERIALIZER = new GsonBuilder().enableComplexMapKeySerialization().create();
    private static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
    private static final String INTEREST_STORAGE_PREFERENCE = "INTEREST_STORAGE_PREFERENCE";

    private final SharedPreferences interestPreferences;
    private final Context context;

    public InterestPreferences(Context context) {
        this.interestPreferences = context.getSharedPreferences(INTEREST_PREFERENCES, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void setInterestStorage(InterestStorage interestStorage) {
        interestPreferences.edit()
                .putString(INTEREST_STORAGE_PREFERENCE, GSON_SERIALIZER.toJson(interestStorage))
                .apply();
    }

    public InterestStorage getInterestStorage() {
        final String jsonInterestStorage = interestPreferences.getString(INTEREST_STORAGE_PREFERENCE, null);
        return jsonInterestStorage == null
                ? InterestStorage.getDefault(context)
                : GSON_SERIALIZER.fromJson(jsonInterestStorage, InterestStorage.class);
    }
}
