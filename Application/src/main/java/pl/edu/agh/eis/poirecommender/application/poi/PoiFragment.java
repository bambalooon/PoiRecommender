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

    private Poi poi;
    private LoadPoiRatingTask loadPoiRatingTask;
    private SetPoiRatingTask setPoiRatingTask;
    private AddVisitTask addVisitTask;

    @Bind(R.id.poi_name) TextView nameText;
    @Bind(R.id.poi_lat) TextView latitudeText;
    @Bind(R.id.poi_lon) TextView longitudeText;

    @Bind(R.id.rating_bar) RatingBar ratingBar;
    RatingBarDecorator ratingBarDecorator;
    @Bind(R.id.save_rating_btn) ImageButton saveRatingButton;
    @Bind(R.id.remove_rating_btn) ImageButton removeRatingButton;
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.check_in_btn) Button checkInPoiButton;

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
        checkInPoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVisit(poi);
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

    private void addVisit(Poi poi) {
        cancelAddVisitTask();
        addVisitTask = new AddVisitTask(this);
        addVisitTask.execute(poi);
    }

    private void cancelAddVisitTask() {
        if (addVisitTask != null) {
            addVisitTask.cancel(true);
            addVisitTask = null;
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
        cancelAddVisitTask();
    }
}
