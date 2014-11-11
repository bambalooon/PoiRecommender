package pl.edu.agh.eis.poirecommender.application.interests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.common.base.Preconditions;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestArrayAdapter extends ArrayAdapter<Interest> {
    private final InterestPreferences interestPreferences;
    private final List<Interest> interestList;

    public static InterestArrayAdapter newInstance(Context context) {
        InterestPreferences interestPreferences = new InterestPreferences(context);
        return new InterestArrayAdapter(context, interestPreferences, interestPreferences.getInterests());
    }

    protected InterestArrayAdapter(Context context, InterestPreferences interestPreferences, List<Interest> interests) {
        super(context, R.layout.interest_row, interests);
        this.interestPreferences = interestPreferences;
        this.interestList = interests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interest_row, parent, false);
        }
        final Interest interest = interestList.get(position);
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
                Preconditions.checkState(
                        interestPreferences.modifyInterest(interest),
                        "Interest preference modification fail!");
                RecommenderService.notifyRecommender(getContext());
            }
        });
        TextView interestName = (TextView) convertView.findViewById(R.id.interest_name);
        interestName.setText(interest.getValue());
        return convertView;
    }
}
