package pl.edu.agh.eis.poirecommender.application.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;

import java.util.List;

/**
 * Created by BamBalooon on 2015-03-06.
 */
public class NavigationDrawerItemsArrayAdapter extends ArrayAdapter<NavigationDrawerItem> {
    public NavigationDrawerItemsArrayAdapter(Context context, List<NavigationDrawerItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavigationDrawerItem item = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView titleText = (TextView) convertView.findViewById(R.id.item_title);
        ImageView iconImage = (ImageView) convertView.findViewById(R.id.item_icon);
        titleText.setText(item.getTitleResource());
        iconImage.setImageResource(item.getIconResource());
        return convertView;
    }
}
