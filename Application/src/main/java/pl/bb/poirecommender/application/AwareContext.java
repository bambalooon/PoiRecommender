package pl.bb.poirecommender.application;

import pl.bb.poirecommender.application.aware.Activity;
import pl.bb.poirecommender.application.aware.Weather;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by BamBalooon on 2014-10-19.
 */
public class AwareContext {
    private static AwareContext INSTANCE;
    public static AwareContext getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AwareContext();
        }
        return INSTANCE;
    }
    private List<Activity> activities = new LinkedList<>();
    private List<Weather> weathers = new LinkedList<>();
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

    public void addActivity(Activity activity) {
        activities.add(activity);
        notifyAwareFragment();
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addWeather(Weather weather) {
        weathers.add(weather);
        notifyAwareFragment();
    }

    public List<Weather> getWeathers() {
        return weathers;
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
