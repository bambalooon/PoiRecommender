package pl.edu.agh.eis.poirecommender.poi_rating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import nl.qbusict.cupboard.DatabaseCompartment;
import pl.edu.agh.eis.poirecommender.dao.DbHelper;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;
import pl.edu.agh.eis.poirecommender.utils.DateTimeProvider;

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

    public void ratePoi(long poiId, Rating poiRating) {
        DatabaseCompartment db = cupboard().withDatabase(this.db);
        PoiRating currentPoiRating = getPoiRating(poiId);
        if (currentPoiRating != null) {
            db.delete(PoiRating.class, currentPoiRating.get_id());
        }
        db.put(new PoiRating(null, dateTimeProvider.getTimestamp(), poiId, poiRating));
    }
}
