package pl.edu.agh.eis.poirecommender.dao;

import nl.qbusict.cupboard.QueryResultIterable;

import java.util.ArrayList;
import java.util.List;

public class CupboardHelper {
    public static <T> List<T> getListFromQueryResultIterator(QueryResultIterable<T> iterator) {
        List<T> items = new ArrayList<>();
        for (T item : iterator) {
            items.add(item);
        }
        iterator.close();

        return items;
    }
}
