package pl.edu.agh.eis.poirecommender.visit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.storage.ContextStorage;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.plugin.openweather.Provider;
import com.aware.poirecommender.provider.PoiRecommenderContract;
import pl.edu.agh.eis.poirecommender.dao.CupboardHelper;
import pl.edu.agh.eis.poirecommender.dao.DbHelper;
import pl.edu.agh.eis.poirecommender.dao.Visit;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.DayPartAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.RainAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.TemperatureAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WeekDayAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WindAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.utils.DateTimeProvider;

import java.util.Date;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class VisitService {
    private final SQLiteDatabase db;
    private final DateTimeProvider dateTimeProvider;
    private ContextStorage<GenericContextProperty> contextStorage;

    public VisitService(Context context) {
        db = new DbHelper(context).getWritableDatabase();
        dateTimeProvider = new DateTimeProvider();
        contextStorage = new com.aware.context.provider.Context(context.getContentResolver(),
                new ContextPropertySerialization<>(GenericContextProperty.class));
    }

    private void addVisit(Visit visit) {
        cupboard().withDatabase(this.db).put(visit);
    }

    public void visit(Poi poi) {
        GenericContextProperty weatherContextProperty = contextStorage
                .getContextProperty(PoiRecommenderContract.Contexts.PLUGIN_OPENWEATHER_TIMESTAMP);
        Date date = dateTimeProvider.getDate();

        for (PoiType poiType : PoiTypeToConstraintMap.getPoiType(poi)) {
            addVisit(new Visit(null,
                    new DayPartAdapter(date).adaptValue(),
                    new WeekDayAdapter(date).adaptValue(),
                    new TemperatureAdapter(weatherContextProperty, Provider.OpenWeather_Data.TEMPERATURE).adaptValue(),
                    new WindAdapter(weatherContextProperty, Provider.OpenWeather_Data.WIND_SPEED).adaptValue(),
                    new RainAdapter(weatherContextProperty, Provider.OpenWeather_Data.RAIN).adaptValue(),
                    poiType));
        }
    }

    public List<Visit> getVisits() {
        return CupboardHelper.getListFromQueryResultIterator(cupboard().withDatabase(db)
                .query(Visit.class).query());
    }
}
