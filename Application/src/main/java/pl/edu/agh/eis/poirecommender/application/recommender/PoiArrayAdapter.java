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
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.CardinalDirection;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Collections;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Name: PoiArrayAdapter
 * Description: PoiArrayAdapter
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class PoiArrayAdapter extends ArrayAdapter<PoiAtDistanceWithDirection> {
    //FIXME: add loader to load location in other thread
    private static final String DISTANCE_FORMAT = "%.0f%s";
    private static final String DISTANCE_UNIT = "m";
    private final PoiManager poiManager;
    private final LocationHolder locationHolder;
    private List<PoiAtDistanceWithDirection> poiList;

    public PoiArrayAdapter(Activity activity) {
        super(activity, R.layout.row_poi);
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
    public PoiAtDistanceWithDirection getItem(int position) {
        return poiList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_poi, parent, false);
        }
        PoiAtDistanceWithDirection poi = getItem(position);
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
        //TODO: should accept no location - it'd show POIs without distance and direction
        this.poiList = poiStorage == null || location == null
                ? Collections.<PoiAtDistanceWithDirection>emptyList()
                : FluentIterable.from(poiStorage.getPoiList())
                    .transform(new PoiAtDistance.AttachLocationToPoi(location))
                    .transform(PoiAtDistanceWithDirection.ATTACH_DIRECTION_TO_POI)
                    .toSortedList(PoiAtDistanceWithDirection.DISTANCE_COMPARATOR);
    }
}
