package pl.edu.agh.eis.poirecommender.application.main;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {
    private static final String APP_PREFERENCES = "APPLICATION_PREFERENCES";
    private static final String AUTH_TOKEN = "auth_token";
    private final SharedPreferences preferences;

    public ApplicationPreferences(Context context) {
        this.preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setAuthToken(String authToken) {
        preferences.edit().putString(AUTH_TOKEN, authToken).apply();
    }

    public String getAuthToken() {
        return preferences.getString(AUTH_TOKEN, null);
    }
}
