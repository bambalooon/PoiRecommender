package pl.edu.agh.eis.poirecommender.application.poi;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.aware.poirecommender.transform.Serializer;
import com.google.common.base.Preconditions;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

/**
 * Name: PoiFragment
 * Description: PoiFragment
 * Date: 2015-05-03
 * Created by BamBalooon
 */
public class PoiFragment extends Fragment {
    private static final String ARGUMENT_POI_ELEMENT = "ARGUMENT_POI_ELEMENT";
    /**
     * TODO: move request code constant to specific class with global request codes for pending intents
     * because starting pending intent with same request code cancels or updates previous one
     * According to: http://codetheory.in/android-pending-intents/
     */
    private static final int ACTION_STORE_POI_WITH_CONTEXT_REQUEST_CODE = 0;

    private Poi mPoi;
    private PoiRecommenderServiceInvoker mServiceInvoker;
    private RatingProvider mRatingProvider;

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
        Element poiElement = new Serializer<>(Element.class)
                .deserialize(getArguments().getString(ARGUMENT_POI_ELEMENT));
        Preconditions.checkNotNull(poiElement, "PoiFragment cannot be created without Element.");
        mPoi = OsmPoi.fromOsmElement(poiElement);
        mServiceInvoker = new PoiRecommenderServiceInvoker(
                getActivity().getApplicationContext(),
                ACTION_STORE_POI_WITH_CONTEXT_REQUEST_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poi_fragment, container, false);

        TextView poiNameTextView = (TextView) view.findViewById(R.id.poi_name);
        TextView poiLatitudeTextView = (TextView) view.findViewById(R.id.poi_lat);
        TextView poiLongitudeTextView = (TextView) view.findViewById(R.id.poi_lon);
        RatingBar poiRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        Button checkInPoiButton = (Button) view.findViewById(R.id.poi_select);

        poiNameTextView.setText(mPoi.getName());
        Location poiLocation = mPoi.getLocation();
        poiLatitudeTextView.setText(Double.toString(poiLocation.getLatitude()));
        poiLongitudeTextView.setText(Double.toString(poiLocation.getLongitude()));
        mRatingProvider = new RatingProvider(poiRatingBar, getResources().getInteger(R.integer.poi_rating_stars_count));
        checkInPoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceInvoker.storeAndRatePoiWithContext(mPoi.getElement(), mRatingProvider.getRating());
            }
        });
        return view;
    }

    private class RatingProvider {
        private final RatingBar ratingBar;
        private final int ratingBarStarsCount;

        private RatingProvider(RatingBar ratingBar, int ratingBarStarsCount) {
            this.ratingBar = ratingBar;
            this.ratingBarStarsCount = ratingBarStarsCount;
        }

        public double getRating() {
            return (ratingBar.getRating() - 1) / (ratingBarStarsCount - 1);
        }
    }
}
