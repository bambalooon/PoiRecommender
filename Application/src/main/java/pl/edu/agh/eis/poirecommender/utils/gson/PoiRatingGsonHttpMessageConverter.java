package pl.edu.agh.eis.poirecommender.utils.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class PoiRatingGsonHttpMessageConverter extends GsonHttpMessageConverter {
    public PoiRatingGsonHttpMessageConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
                    @Override
                    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {
                    @Override
                    public JsonElement serialize(Timestamp timestamp, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(timestamp.getTime());
                    }
                });
        setGson(gsonBuilder.create());
    }
}
