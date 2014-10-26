package pl.bb.poirecommender.application.aware;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareContext {
    private static AwareContext INSTANCE;
    public static AwareContext getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AwareContext();
        }
        return INSTANCE;
    }
    private List<Object> awareNotifications = new LinkedList<>();
    private AwareFragment awareFragment;

    private AwareContext() {
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
