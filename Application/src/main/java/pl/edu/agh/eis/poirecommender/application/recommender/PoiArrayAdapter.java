package pl.edu.agh.eis.poirecommender.application.recommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;

import java.util.Collections;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiArrayAdapter extends ArrayAdapter<PoiAtDistance> {
    private static final String DISTANCE_UNIT = "km";
    private final PoiManager poiManager;
    private List<PoiAtDistance> poiList;

    public PoiArrayAdapter(Context context) {
        super(context, R.layout.poi_row);
        this.poiManager = new PoiManager(context);
        PoiStorage poiStorage = poiManager.getPoiStorage();
        this.poiList = poiStorage == null
                ? Collections.<PoiAtDistance>emptyList()
                : poiStorage.getPoiList();
    }

    @Override
    public void notifyDataSetChanged() {
        PoiStorage poiStorage = poiManager.getPoiStorage();
        this.poiList = poiStorage == null
                ? Collections.<PoiAtDistance>emptyList()
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
        PoiAtDistance poi = poiList.get(position);
        final TextView poiNameText = (TextView) convertView.findViewById(R.id.poi_name);
        final TextView poiDistanceText = (TextView) convertView.findViewById(R.id.poi_distance);
        poiNameText.setText(poi.getName());
        poiDistanceText.setText(String.format("%.1f%s", poi.getDistance(), DISTANCE_UNIT));
        return convertView;
    }
}
