package pl.edu.agh.eis.poirecommender.pois.gson;

import com.google.gson.*;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

import java.lang.reflect.Type;

/**
 * Created by Krzysztof Balon on 2014-11-14.
 */
public class GsonPoiAdapter implements JsonSerializer<Poi>, JsonDeserializer<Poi> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public JsonElement serialize(Poi src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serializedObject = new JsonObject();
        String className = src.getClass().getCanonicalName();
        serializedObject.addProperty(CLASSNAME, className);
        JsonElement poiElement = context.serialize(src);
        serializedObject.add(INSTANCE, poiElement);
        return serializedObject;
    }

    @Override
    public Poi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject serializedObject = json.getAsJsonObject();
        JsonPrimitive classNameProperty = serializedObject.getAsJsonPrimitive(CLASSNAME);
        String className = classNameProperty.getAsString();
        Class<?> cls;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(serializedObject.get(INSTANCE), cls);
    }
}
