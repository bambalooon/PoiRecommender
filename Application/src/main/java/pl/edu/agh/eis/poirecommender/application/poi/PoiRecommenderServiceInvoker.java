package pl.edu.agh.eis.poirecommender.application.poi;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.aware.poirecommender.service.PoiRecommenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PoiRecommenderServiceInvoker {
    private final Context context;
    private final int actionStoreContextRequestCode;

    public void storeContext(long poiId) {
        Intent actionIntent = new Intent(PoiRecommenderService.ACTION_STORE_CONTEXT);
        actionIntent.putExtra(
                PoiRecommenderService.POI_ID_EXTRA,
                poiId);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                actionStoreContextRequestCode,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send();
            log.debug("Context stored with POI {}", poiId);
        } catch (PendingIntent.CanceledException e) {
            log.error("Sending pending intent failed...", e);
        }
    }
}
