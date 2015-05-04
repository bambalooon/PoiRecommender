package pl.edu.agh.eis.poirecommender.application.poi;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.aware.poirecommender.service.PoiRecommenderService;
import com.aware.poirecommender.transform.Serializer;
import com.google.common.base.Preconditions;
import pl.edu.agh.eis.poirecommender.R;

/**
 * Name: PoiFragment
 * Description: PoiFragment
 * Date: 2015-05-03
 * Created by BamBalooon
 */
public class PoiFragment extends Fragment {
    private static final String TAG = PoiFragment.class.getSimpleName();
    private static final String ARGUMENT_POI_ELEMENT = "ARGUMENT_POI_ELEMENT";
    /**
     * TODO: move request code constant to specific class with global request codes for pending intents
     * because starting pending intent with same request code cancels or updates previous one
     * According to: http://codetheory.in/android-pending-intents/
     */
    private static final int ACTION_STORE_POI_WITH_CONTEXT_REQUEST_CODE = 0;

    private Element osmElement;

    public static PoiFragment newInstance(Element poiElement) {
        PoiFragment poiFragment = new PoiFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_POI_ELEMENT, new Serializer<>(Element.class).serialize(poiElement));
        poiFragment.setArguments(args);
        return poiFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        osmElement = new Serializer<>(Element.class)
                .deserialize(getArguments().getString(ARGUMENT_POI_ELEMENT));
        Preconditions.checkNotNull(osmElement, "PoiFragment cannot be created without Element.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poi_fragment, container, false);
        TextView poiNameText = (TextView) view.findViewById(R.id.poi_name);
        TextView poiLatText = (TextView) view.findViewById(R.id.poi_lat);
        TextView poiLonText = (TextView) view.findViewById(R.id.poi_lon);
        Button poiSelectButton = (Button) view.findViewById(R.id.poi_select);
        poiNameText.setText(osmElement.getTags().get(Element.ELEMENT_NAME_TAG));
        poiLatText.setText(Double.toString(osmElement.getLat()));
        poiLonText.setText(Double.toString(osmElement.getLon()));
        poiSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveContextIntent = new Intent(PoiRecommenderService.ACTION_STORE_POI_WITH_CONTEXT);
                saveContextIntent.putExtra(PoiRecommenderService.POI_EXTRA, new Serializer<>(Element.class).serialize(osmElement));
                PendingIntent pendingIntent = PendingIntent.getService(
                        getActivity().getApplicationContext(),
                        ACTION_STORE_POI_WITH_CONTEXT_REQUEST_CODE,
                        saveContextIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "Sending pending intent failed...", e);
                }
            }
        });
        return view;
    }
}
