package pl.edu.agh.eis.poirecommender.poi_rating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import nl.qbusict.cupboard.DatabaseCompartment;
import nl.qbusict.cupboard.QueryResultIterable;
import pl.edu.agh.eis.poirecommender.dao.DbHelper;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;
import pl.edu.agh.eis.poirecommender.utils.DateTimeProvider;

import java.util.ArrayList;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class PoiRatingService {
    private final SQLiteDatabase db;
    private final DateTimeProvider dateTimeProvider;

    public PoiRatingService(Context context) {
        db = new DbHelper(context).getWritableDatabase();
        dateTimeProvider = new DateTimeProvider();
    }

    public PoiRating getPoiRating(Long poiId) {
        return cupboard().withDatabase(db)
                .query(PoiRating.class).withSelection("poiId=?", poiId.toString()).get();
    }

    public List<PoiRating> getPoiRatings() {
        return getListFromQueryResultIterator(cupboard().withDatabase(db)
                .query(PoiRating.class).query());
    }

    public void addPoiRating(PoiRating poiRating) {
        DatabaseCompartment db = cupboard().withDatabase(this.db);

        PoiRating currentPoiRating = getPoiRating(poiRating.getPoiId());
        if (currentPoiRating != null) {
            db.delete(PoiRating.class, currentPoiRating.get_id());
        }

        db.put(poiRating);
    }

    public void addPoiRatings(Iterable<PoiRating> poiRatings) {
        for (PoiRating poiRating : poiRatings) {
            addPoiRating(poiRating);
        }
    }

    public void ratePoi(long poiId, Rating poiRating) {
        addPoiRating(new PoiRating(null, dateTimeProvider.getTimestamp(), poiId, poiRating));
    }

    private static <T> List<T> getListFromQueryResultIterator(QueryResultIterable<T> iterator) {
        List<T> items = new ArrayList<>();
        for (T item : iterator) {
            items.add(item);
        }
        iterator.close();

        return items;
    }
}
