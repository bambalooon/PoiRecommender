package pl.edu.agh.eis.poirecommender.application.recommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.aware.AwarePreferences;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiArrayAdapter extends ArrayAdapter<PoiAtDistanceWithDirection> {
    private static final String DISTANCE_FORMAT = "%.1f%s";
    private static final String DISTANCE_UNIT = "km";
    private final PoiManager poiManager;
    private final AwarePreferences awarePreferences;
    private List<PoiAtDistanceWithDirection> poiList;

    public PoiArrayAdapter(Context context) {
        super(context, R.layout.poi_row);
        this.awarePreferences = new AwarePreferences(context);
        this.poiManager = new PoiManager(context);
        this.poiList = setUpPoiList();
    }

    @Override
    public void notifyDataSetChanged() {
        this.poiList = setUpPoiList();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private List<PoiAtDistanceWithDirection> setUpPoiList() {
        final PoiStorage poiStorage = poiManager.getPoiStorage();
        final Location location = awarePreferences.getLocation();
        return poiStorage == null || location == null
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
