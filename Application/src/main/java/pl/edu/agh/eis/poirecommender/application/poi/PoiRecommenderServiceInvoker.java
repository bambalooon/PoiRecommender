package pl.edu.agh.eis.poirecommender.application.poi;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.aware.poirecommender.service.PoiRecommenderService;
import com.aware.poirecommender.transform.Serializer;

/**
 * Name: PoiRecommenderServiceInvoker
 * Description: PoiRecommenderServiceInvoker
 * Date: 2015-05-09
 * Created by BamBalooon
 */
public class PoiRecommenderServiceInvoker {
    private static final String TAG = PoiRecommenderServiceInvoker.class.getSimpleName();
    private final Context context;
    private final int actionStoreAndRatePoiWithContextRequestCode;

    public PoiRecommenderServiceInvoker(Context context, int actionStoreAndRatePoiWithContextRequestCode) {
        this.context = context;
        this.actionStoreAndRatePoiWithContextRequestCode = actionStoreAndRatePoiWithContextRequestCode;
    }

    public void storeAndRatePoiWithContext(Element poiElement, double poiRating) {
        Intent actionIntent = new Intent(PoiRecommenderService.ACTION_STORE_AND_RATE_POI_WITH_CONTEXT);
        actionIntent.putExtra(
                PoiRecommenderService.POI_EXTRA,
                new Serializer<>(Element.class).serialize(poiElement));
        actionIntent.putExtra(
                PoiRecommenderService.RATING_EXTRA,
                poiRating);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                actionStoreAndRatePoiWithContextRequestCode,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "Sending pending intent failed...", e);
        }
    }
}
