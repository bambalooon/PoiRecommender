package pl.edu.agh.eis.poirecommender.application;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.model.CardinalDirection;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.AsyncResult;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class AbstractPoiListAdapter extends ArrayAdapter<PoiAtDistanceWithDirection> {
    protected static final String DISTANCE_FORMAT = "%.0f%s";
    protected static final String DISTANCE_UNIT = "m";
    protected AsyncResult<? extends List<PoiAtDistanceWithDirection>> poiList;

    protected AbstractPoiListAdapter(Activity activity) {
        super(activity, R.layout.row_poi);
    }

    @Override
    public int getCount() {
        return poiList != null
                ? poiList.getData().isPresent() ? poiList.getData().get().size() : 0
                : 0;
    }

    @Override
    public PoiAtDistanceWithDirection getItem(int position) {
        return poiList != null
                ? poiList.getData().isPresent() ? poiList.getData().get().get(position) : null
                : null;
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
}
