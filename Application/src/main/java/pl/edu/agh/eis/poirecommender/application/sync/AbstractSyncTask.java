package pl.edu.agh.eis.poirecommender.application.sync;

import android.os.AsyncTask;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.edu.agh.eis.poirecommender.application.main.ApplicationPreferences;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractSyncTask extends AsyncTask<Void, Void, AbstractSyncTask.SyncResult> {
    private final ApplicationPreferences preferences;
    private final SyncResultJoinNotifier syncResultJoinNotifier;

    protected MultiValueMap<String, String> createHeaders() {
        String basicAuthToken = preferences.getAuthToken();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + basicAuthToken);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected void onPostExecute(SyncResult syncResult) {
        syncResultJoinNotifier.setResult(syncResult);
    }

    enum  SyncResult {
        SUCCESS, FAIL
    }
}
