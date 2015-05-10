package pl.edu.agh.eis.poirecommender.application.debug;

import android.app.Activity;
import android.widget.ArrayAdapter;
import com.aware.context.property.GenericContextProperty;
import pl.edu.agh.eis.poirecommender.R;

import java.util.List;

/**
 * Name: ContextAdapter
 * Description: ContextAdapter
 * Date: 2015-04-17
 * Created by BamBalooon
 */
public class ContextAdapter extends ArrayAdapter<GenericContextProperty> {
    private List<GenericContextProperty> contextProperties;

    public ContextAdapter(Activity activity) {
        super(activity, R.layout.row_aware, R.id.aware_row_text);
    }

    @Override
    public int getCount() {
        if (contextProperties != null) {
            return contextProperties.size();
        } else {
            return 0;
        }
    }

    @Override
    public GenericContextProperty getItem(int position) {
        if (contextProperties != null) {
            return contextProperties.get(position);
        } else {
            return null;
        }
    }

    public void swapContextProperties(List<GenericContextProperty> contextProperties) {
        this.contextProperties = contextProperties;
        if (contextProperties != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }
}
