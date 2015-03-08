package pl.edu.agh.eis.poirecommender.application.interests;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;
import pl.edu.agh.eis.poirecommender.interests.InterestStorage;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestArrayAdapter extends ArrayAdapter<Interest> {
    private final InterestPreferences interestPreferences;
    private final InterestStorage interestStorage;

    public static InterestArrayAdapter newInstance(Activity activity) {
        InterestPreferences interestPreferences = new InterestPreferences(activity.getApplicationContext());
        return new InterestArrayAdapter(activity, interestPreferences, interestPreferences.getInterestStorage());
    }

    protected InterestArrayAdapter(Activity activity, InterestPreferences interestPreferences, InterestStorage interestStorage) {
        super(activity, R.layout.interest_row, interestStorage.getInterests());
        this.interestPreferences = interestPreferences;
        this.interestStorage = interestStorage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interest_row, parent, false);
        }
        final Interest interest = interestStorage.getInterests().get(position);
        SeekBar certaintyBar = (SeekBar) convertView.findViewById(R.id.certainty_bar);
        certaintyBar.setProgress(interest.getCertainty());
        certaintyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                interest.setCertainty(seekBar.getProgress());
                interestStorage.modifyInterest(interest);
                interestPreferences.setInterestStorage(interestStorage);
                RecommenderService.notifyRecommender(getContext().getApplicationContext());
            }
        });
        TextView interestName = (TextView) convertView.findViewById(R.id.interest_name);
        interestName.setText(interest.getValue());
        return convertView;
    }
}
