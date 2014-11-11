package pl.edu.agh.eis.poirecommender.application.recommender;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import pl.edu.agh.eis.poirecommender.R;

public class RecommenderActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);
    }
}
