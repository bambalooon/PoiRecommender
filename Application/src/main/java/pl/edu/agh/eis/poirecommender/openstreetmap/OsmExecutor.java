package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.aware.poirecommender.transform.Serializer;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class OsmExecutor {
    private static final String TAG = OsmExecutor.class.getSimpleName();
    private static final String OPEN_STREET_MAP_API_URL_STRING = "http://overpass-api.de/api/interpreter?data=";
    private static final String RESPONSE_ENCODING = "UTF-8";
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 30 * 1000;
    private static final int CONNECTION_TIMEOUT = 10 * 1000;

    private final Serializer<OsmResponse> responseSerializer = new Serializer<>(OsmResponse.class);

    public OsmResponse execute(OsmRequest osmRequest, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting()
                ? execute(osmRequest)
                : null;
    }

    public OsmResponse execute(OsmRequest osmRequest) {
        InputStream in = null;
        try {
            final URL url = new URL(OPEN_STREET_MAP_API_URL_STRING + osmRequest);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.setDoInput(true);

            urlConnection.connect();
            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection exception for url: " + url.toString() + ", status code: " + urlConnection.getResponseCode());
                return null;
            }
            in = urlConnection.getInputStream();
            final String jsonResponse = CharStreams.toString(new InputStreamReader(in, RESPONSE_ENCODING));
            return responseSerializer.deserialize(jsonResponse);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage() + "\n" + FluentIterable.from(ImmutableList.copyOf(e.getStackTrace()))
                    .join(Joiner.on('\n')));
            return null;
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
    }
}
