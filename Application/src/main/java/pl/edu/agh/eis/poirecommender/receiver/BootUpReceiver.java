package pl.edu.agh.eis.poirecommender.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.aware.AwareContextObservingService;

/**
 * Name: BootUpReceiver
 * Description: BootUpReceiver
 * Date: 2015-03-14
 * Created by BamBalooon
 */
@Slf4j
public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent awareContextObservingServiceStartupIntent = new Intent(context, AwareContextObservingService.class);
        context.startService(awareContextObservingServiceStartupIntent);
        log.debug("BootUp hook invoked - AwareContextObservingService started");
    }
}
