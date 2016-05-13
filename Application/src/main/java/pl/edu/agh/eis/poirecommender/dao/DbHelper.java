package pl.edu.agh.eis.poirecommender.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "poi_recommender.db";
    private static final int DATABASE_VERSION = 2;

    static {
        cupboard().register(PoiRating.class);
        cupboard().register(Visit.class);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cupboard annotatedCupboard = new CupboardBuilder(cupboard()).useAnnotations().build();
        annotatedCupboard.withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Cupboard annotatedCupboard = new CupboardBuilder(cupboard()).useAnnotations().build();
        annotatedCupboard.withDatabase(db).upgradeTables();
    }
}
