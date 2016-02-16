package pl.edu.agh.eis.poirecommender;

import android.content.Context;
import com.github.fernandodev.androidproperties.lib.AssetsProperties;
import com.github.fernandodev.androidproperties.lib.Property;
import lombok.Getter;

@Getter
public class Config extends AssetsProperties {
    @Property("poi_recommender_login_url")
    private String poiRecommenderLoginUrl;

    public Config(Context context) {
        super(context);
    }
}
