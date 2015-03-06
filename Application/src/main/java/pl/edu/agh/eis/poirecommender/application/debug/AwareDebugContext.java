package pl.edu.agh.eis.poirecommender.application.debug;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareDebugContext {
    private static AwareDebugContext INSTANCE;
    public static AwareDebugContext getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AwareDebugContext();
        }
        return INSTANCE;
    }
    private List<Object> awareNotifications = new LinkedList<>();
    private AwareFragment awareFragment;

    private AwareDebugContext() {
    }

    public void registerAwareFragment(AwareFragment awareFragment) {
        this.awareFragment = awareFragment;
    }

    public void unregisterAwareFragment() {
        awareFragment = null;
    }

    public void addAwareNotification(Object notification) {
        awareNotifications.add(notification);
        notifyAwareFragment();
    }

    public List<Object> getAwareNotifications() {
        return awareNotifications;
    }

    private void notifyAwareFragment() {
        if(awareFragment != null) {
            awareFragment.update();
        }
    }
}
