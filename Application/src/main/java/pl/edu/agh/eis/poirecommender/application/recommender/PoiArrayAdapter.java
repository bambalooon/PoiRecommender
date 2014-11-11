package pl.edu.agh.eis.poirecommender.application.recommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.CardinalDirection;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;

import java.util.Collections;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiArrayAdapter extends ArrayAdapter<PoiAtDistanceWithDirection> {
    private static final String DISTANCE_FORMAT = "%.1f%s";
    private static final String DISTANCE_UNIT = "km";
    private final PoiManager poiManager;
    private List<PoiAtDistanceWithDirection> poiList;

    public PoiArrayAdapter(Context context) {
        super(context, R.layout.poi_row);
        this.poiManager = new PoiManager(context);
        PoiStorage poiStorage = poiManager.getPoiStorage();
        this.poiList = poiStorage == null
                ? Collections.<PoiAtDistanceWithDirection>emptyList()
                : poiStorage.getPoiList();
    }

    @Override
    public void notifyDataSetChanged() {
        PoiStorage poiStorage = poiManager.getPoiStorage();
        this.poiList = poiStorage == null
                ? Collections.<PoiAtDistanceWithDirection>emptyList()
                : poiStorage.getPoiList();
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
}
