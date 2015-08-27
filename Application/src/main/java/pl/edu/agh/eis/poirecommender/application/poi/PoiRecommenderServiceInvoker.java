package pl.edu.agh.eis.poirecommender.application.poi;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.aware.poirecommender.service.PoiRecommenderService;
import lombok.extern.slf4j.Slf4j;

/**
 * Name: PoiRecommenderServiceInvoker
 * Description: PoiRecommenderServiceInvoker
 * Date: 2015-05-09
 * Created by BamBalooon
 */
@Slf4j
public class PoiRecommenderServiceInvoker {
    private final Context context;
    private final int actionRatePoiRequestCode;
    private final int actionStoreContextRequestCode;

    public PoiRecommenderServiceInvoker(Context context,
                                        int actionRatePoiRequestCode,
                                        int actionStoreContextRequestCode) {
        this.context = context;
        this.actionRatePoiRequestCode = actionRatePoiRequestCode;
        this.actionStoreContextRequestCode = actionStoreContextRequestCode;
    }

    public void ratePoi(long poiId, double poiRating) {
        Intent actionIntent = new Intent(PoiRecommenderService.ACTION_RATE_POI);
        actionIntent.putExtra(
                PoiRecommenderService.POI_ID_EXTRA,
                poiId);
        actionIntent.putExtra(
                PoiRecommenderService.RATING_EXTRA,
                poiRating);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                actionRatePoiRequestCode,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send();
            log.debug("POI {} rated: {}", poiId, poiRating);
        } catch (PendingIntent.CanceledException e) {
            log.error("Sending pending intent failed...", e);
        }
    }

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
