package pl.edu.agh.eis.poirecommender.aware;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import com.aware.context.provider.ContextContract;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

/**
 * Name: AwareContextObservingService
 * Description: AwareContextObservingService
 * Date: 2015-03-14
 * Created by BamBalooon
 */
@Slf4j
public class AwareContextObservingService extends Service {
    private HandlerThread handlerThread;
    private ContentObserver contextObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        handlerThread = new HandlerThread(AwareContextObservingService.class.getSimpleName());
        handlerThread.start();
        Handler contextChangeHandler = new Handler(handlerThread.getLooper());

        contextObserver = new ContentObserver(contextChangeHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                RecommenderService.notifyRecommender(getApplicationContext());
            }
        };
        getContentResolver().registerContentObserver(ContextContract.Properties.CONTENT_URI, true, contextObserver);
        log.debug("Service created!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(contextObserver);
        contextObserver = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
        log.debug("Service destroyed!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
