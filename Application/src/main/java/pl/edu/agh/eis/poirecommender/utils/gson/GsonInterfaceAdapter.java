package pl.edu.agh.eis.poirecommender.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Krzysztof Balon on 2015-01-18.
 */
public class GsonInterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serializedObject = new JsonObject();
        String className = src.getClass().getCanonicalName();
        serializedObject.addProperty(CLASSNAME, className);
        JsonElement element = context.serialize(src);
        serializedObject.add(INSTANCE, element);
        return serializedObject;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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
