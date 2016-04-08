package pl.edu.agh.eis.poirecommender.application.sync;

import android.content.Context;
import android.widget.Toast;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.sync.AbstractSyncTask.SyncResult;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SyncResultJoinNotifier {
    private final Context context;
    private final List<SyncResult> syncResults = new ArrayList<>(2);

    public void setResult(SyncResult syncResult) {
        syncResults.add(syncResult);

        if (syncResults.size() == 2) {
            Toast.makeText(
                    context,
                    syncResults.contains(SyncResult.FAIL) ? R.string.sync_failed : R.string.sync_success,
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
