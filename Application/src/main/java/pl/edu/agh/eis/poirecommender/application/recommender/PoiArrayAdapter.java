package pl.edu.agh.eis.poirecommender.application.recommender;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.transform.ContextPropertySerialization;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.*;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
//FIXME: add loader to load location in other thread
public class PoiArrayAdapter extends ArrayAdapter<PoiAtDistanceWithDirection> {
    private static final String DISTANCE_FORMAT = "%.0f%s";
    private static final String DISTANCE_UNIT = "m";
    private final PoiManager poiManager;
    private final LocationHolder locationHolder;
    private List<PoiAtDistanceWithDirection> poiList;

    public PoiArrayAdapter(Activity activity) {
        super(activity, R.layout.poi_row);
        android.content.Context context = activity.getApplicationContext();
        this.locationHolder = new AwareLocationHolder(
                new Context(context.getContentResolver(),
                        new ContextPropertySerialization<>(GenericContextProperty.class)));
        this.poiManager = new PoiManager(context);
        updatePoiList();
    }

    @Override
    public void notifyDataSetChanged() {
        updatePoiList();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.poi_row, parent, false);
        }
        PoiAtDistanceWithDirection poi = poiList.get(position);
        final TextView poiNameText = (TextView) convertView.findViewById(R.id.poi_name);
        final TextView poiDistanceText = (TextView) convertView.findViewById(R.id.poi_distance);
        final ImageView poiDirectionIcon = (ImageView) convertView.findViewById(R.id.poi_direction_icon);
        final TextView poiDirectionText = (TextView) convertView.findViewById(R.id.poi_direction_text);
        poiNameText.setText(poi.getName());
        poiDistanceText.setText(String.format(DISTANCE_FORMAT, poi.getDistance(), DISTANCE_UNIT));
        final CardinalDirection direction = poi.getDirection();
        poiDirectionIcon.setImageResource(direction.getIconResource());
        poiDirectionText.setText(direction.toString());
        return convertView;
    }

    private void updatePoiList() {
        final PoiStorage poiStorage = poiManager.getPoiStorage();
        final Location location = locationHolder.getLocation();
        this.poiList = poiStorage == null || location == null
                ? Collections.<PoiAtDistanceWithDirection>emptyList()
                : FluentIterable.from(poiStorage.getPoiList())
                    .transform(new AttachLocationToPoi(location))
                    .transform(ATTACH_DIRECTION_TO_POI)
                    .toSortedList(DISTANCE_COMPARATOR);
    }

    private static class AttachLocationToPoi implements Function<Poi, PoiAtDistance> {
        private final Location location;

        private AttachLocationToPoi(Location location) {
            this.location = location;
        }
        @Override
        public PoiAtDistance apply(Poi poi) {
            return new PoiAtDistance(poi, location);
        }
    }
    private static final Function<PoiAtDistance, PoiAtDistanceWithDirection> ATTACH_DIRECTION_TO_POI =
            new Function<PoiAtDistance, PoiAtDistanceWithDirection>() {
                @Override
                public PoiAtDistanceWithDirection apply(PoiAtDistance poi) {
                    return new PoiAtDistanceWithDirection(poi);
                }
            };
    private static final Comparator<AtDistance> DISTANCE_COMPARATOR = new Comparator<AtDistance>() {
        @Override
        public int compare(AtDistance atDistance, AtDistance atDistance2) {
            double comparison = atDistance.getDistance() - atDistance2.getDistance();
            return comparison > 0
                    ? 1
                    : comparison == 0
                    ? 0
                    : -1;
        }
    };
}
