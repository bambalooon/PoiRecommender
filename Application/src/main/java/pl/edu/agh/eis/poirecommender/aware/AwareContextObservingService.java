package pl.edu.agh.eis.poirecommender.aware;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import com.aware.context.observer.ContextPropertyCreator;
import com.aware.context.observer.ContextPropertyMapping;
import com.aware.context.observer.ContextPropertyObserver;
import com.aware.context.positioner.NewRecordsCursorPositioner;
import com.aware.context.processor.ContextPropertyProcessor;
import com.aware.context.property.ContextProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: AwareContextObservingService
 * Description: AwareContextObservingService
 * Date: 2015-03-14
 * Created by BamBalooon
 */
public class AwareContextObservingService extends Service {
    private static final String TAG = AwareContextObservingService.class.getSimpleName();
    private HandlerThread handlerThread;
    private List<ContentObserver> contentObservers;

    @Override
    public void onCreate() {
        super.onCreate();

        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        Handler contextPropertyChangeHandler = new Handler(handlerThread.getLooper());

        ContentResolver contentResolver = getContentResolver();
        contentObservers = new ArrayList<>();
        for (Uri contextPropertyUri : ContextPropertyMapping.getDefaultInstance().getContextPropertyUriList()) {
            ContentObserver contextPropertyObserver = new ContextPropertyObserver<>(
                    contextPropertyChangeHandler,
                    contextPropertyUri,
                    NewRecordsCursorPositioner.createInstancePositionedAtEnd(contextPropertyUri, contentResolver),
                    ContextPropertyCreator.getDefaultInstance(),
                    new ContextPropertyProcessor<ContextProperty>() {
                        @Override
                        public void process(ContextProperty contextProperty) {
                            Log.d(TAG, contextProperty.toString());
                        }
                    });
            contentResolver.registerContentObserver(contextPropertyUri, true, contextPropertyObserver);
            contentObservers.add(contextPropertyObserver);
        }
        Log.d(TAG, "Service created!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ContentResolver contentResolver = getContentResolver();
        for (ContentObserver contextPropertyObserver : contentObservers) {
            contentResolver.unregisterContentObserver(contextPropertyObserver);
        }
        contentObservers = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
        Log.d(TAG, "Service destroyed!");
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
