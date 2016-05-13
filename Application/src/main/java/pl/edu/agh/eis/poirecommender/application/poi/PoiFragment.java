package pl.edu.agh.eis.poirecommender.application.poi;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.aware.poirecommender.transform.Serializer;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;

/**
 * Name: PoiFragment
 * Description: PoiFragment
 * Date: 2015-05-03
 * Created by BamBalooon
 */
@Slf4j
public class PoiFragment extends Fragment {
    private static final String ARGUMENT_POI_ELEMENT = "ARGUMENT_POI_ELEMENT";
    /**
     * TODO: move request code constant to specific class with global request codes for pending intents
     * because starting pending intent with same request code cancels or updates previous one
     * According to: http://codetheory.in/android-pending-intents/
     */
    private static final int ACTION_STORE_CONTEXT_REQUEST_CODE = 0;

    private Poi poi;
    private LoadPoiRatingTask loadPoiRatingTask;
    private SetPoiRatingTask setPoiRatingTask;
    private PoiRecommenderServiceInvoker mServiceInvoker;

    @Bind(R.id.poi_name) TextView nameText;
    @Bind(R.id.poi_lat) TextView latitudeText;
    @Bind(R.id.poi_lon) TextView longitudeText;

    @Bind(R.id.rating_bar) RatingBar ratingBar;
    RatingBarDecorator ratingBarDecorator;
    @Bind(R.id.save_rating_btn) ImageButton saveRatingButton;
    @Bind(R.id.remove_rating_btn) ImageButton removeRatingButton;
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.check_in_btn) Button mCheckInPoiButton;

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
        poi = OsmPoi.fromOsmElement(poiElement);
        mServiceInvoker = new PoiRecommenderServiceInvoker(
                getActivity().getApplicationContext(),
                ACTION_STORE_CONTEXT_REQUEST_CODE);
        log.debug("PoiFragment created for {}", poi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_poi, container, false);
        ButterKnife.bind(this, view);

        nameText.setText(poi.getName());
        Location poiLocation = poi.getLocation();
        latitudeText.setText(Double.toString(poiLocation.getLatitude()));
        longitudeText.setText(Double.toString(poiLocation.getLongitude()));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                saveRatingButton.setVisibility(View.VISIBLE);
            }
        });

        ratingBarDecorator = new RatingBarDecorator(ratingBar);
        ratingBarDecorator.disable();

        saveRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPoiRating(poi.getElement().getId(), ratingBarDecorator.getRating());
            }
        });
        removeRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPoiRating(poi.getElement().getId(), Rating.NONE);
            }
        });
        mCheckInPoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceInvoker.storeContext(poi.getElement().getId());
                //FIXME: message should be displayed after check in ended successfully
                Toast.makeText(getActivity(), R.string.checked_in_message, Toast.LENGTH_SHORT).show();
            }
        });

        loadPoiRating(poi.getElement().getId());
        return view;
    }

    private void setPoiRating(long poiId, Rating rating) {
        cancelSetRatingTask();
        setPoiRatingTask = new SetPoiRatingTask(this);
        setPoiRatingTask.execute(new ImmutablePair<>(poiId, rating));
    }

    private void cancelSetRatingTask() {
        if (setPoiRatingTask != null) {
            setPoiRatingTask.cancel(true);
            setPoiRatingTask = null;
        }
    }

    private void loadPoiRating(long poiId) {
        cancelLoadRatingTask();
        loadPoiRatingTask = new LoadPoiRatingTask(this);
        loadPoiRatingTask.execute(poiId);
    }

    private void cancelLoadRatingTask() {
        if (loadPoiRatingTask != null) {
            loadPoiRatingTask.cancel(true);
            loadPoiRatingTask = null;
        }
    }

    @Override
    public void onDestroy() {
        cancelTasks();
        super.onDestroy();
    }

    private void cancelTasks() {
        cancelLoadRatingTask();
        cancelSetRatingTask();
    }
}
