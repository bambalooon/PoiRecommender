package pl.edu.agh.eis.poirecommender.application.poi;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.aware.poirecommender.provider.PoiRecommenderContract;
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
public class PoiFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARGUMENT_POI_ELEMENT = "ARGUMENT_POI_ELEMENT";
    /**
     * TODO: move request code constant to specific class with global request codes for pending intents
     * because starting pending intent with same request code cancels or updates previous one
     * According to: http://codetheory.in/android-pending-intents/
     */
    private static final int ACTION_STORE_POI_WITH_CONTEXT_REQUEST_CODE = 0;
    private static final int POI_RATING_LOADER = 0;

    private Poi mPoi;
    private PoiRecommenderServiceInvoker mServiceInvoker;
    private ProgressBar mProgressBar;
    private RatingBarDecorator mRatingBarDecorator;
    private Button mCheckInPoiButton;

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
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        RatingBar poiRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        mCheckInPoiButton = (Button) view.findViewById(R.id.poi_select);

        getActivity().getSupportLoaderManager().restartLoader(POI_RATING_LOADER, null, this);

        poiNameTextView.setText(mPoi.getName());
        Location poiLocation = mPoi.getLocation();
        poiLatitudeTextView.setText(Double.toString(poiLocation.getLatitude()));
        poiLongitudeTextView.setText(Double.toString(poiLocation.getLongitude()));
        poiRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mCheckInPoiButton.setEnabled(true);
            }
        });
        mRatingBarDecorator = new RatingBarDecorator(poiRatingBar);
        mCheckInPoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceInvoker.storeAndRatePoiWithContext(mPoi.getElement(), mRatingBarDecorator.getRating());
                mCheckInPoiButton.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                mRatingBarDecorator.disable();
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case POI_RATING_LOADER:
                return new CursorLoader(
                        getActivity(),
                        PoiRecommenderContract.PoisRating.CONTENT_URI,
                        new String[]{PoiRecommenderContract.PoisRating.POI_RATING},
                        PoiRecommenderContract.PoisRating.POI_ID + "=?",
                        new String[]{Long.toString(mPoi.getElement().getId())},
                        PoiRecommenderContract.PoisRating._ID + " DESC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor poiRatingCursor) {
        if (poiRatingCursor != null && poiRatingCursor.moveToFirst()) {
            int poiRatingColumnIndex = poiRatingCursor.getColumnIndex(PoiRecommenderContract.PoisRating.POI_RATING);
            double poiRating = poiRatingCursor.getDouble(poiRatingColumnIndex);
            mRatingBarDecorator.setRating(poiRating);
        }
        mProgressBar.setVisibility(View.INVISIBLE);
        mRatingBarDecorator.enable();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
