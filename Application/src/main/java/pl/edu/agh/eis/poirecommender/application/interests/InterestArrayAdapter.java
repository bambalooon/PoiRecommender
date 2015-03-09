package pl.edu.agh.eis.poirecommender.application.interests;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.interests.InterestStorage;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestArrayAdapter extends ArrayAdapter<Interest> {
    private final InterestStorage interestStorage;
    private final String[] interestNames;

    public InterestArrayAdapter(Activity activity) {
        super(activity, R.layout.interest_row);
        this.interestStorage = new InterestStorage(activity.getApplicationContext());
        this.interestNames = activity.getResources().getStringArray(R.array.interest_names);
    }

    @Override
    public int getCount() {
        return InterestStorage.INTEREST_NAMES.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interest_row, parent, false);
        }

        final Interest interest = interestStorage.getInterest(position);

        TextView interestName = (TextView) convertView.findViewById(R.id.interest_name);
        interestName.setText(interestNames[position]);

        SeekBar interestValueBar = (SeekBar) convertView.findViewById(R.id.interest_value_bar);
        interestValueBar.setProgress(interest.getValue());
        interestValueBar.setOnSeekBarChangeListener(new InterestSeekBarChangeListener(position));
        return convertView;
    }

    private class InterestSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private static final String INTEREST_TITLE_FORMAT = "%s : %d%%";
        private final int position;
        private final ActionBar mActionBar;
        private CharSequence mTitle;

        public InterestSeekBarChangeListener(int position) {
            this.position = position;
            Context context = getContext();
            this.mActionBar = context instanceof ActionBarActivity
                    ? ((ActionBarActivity) context).getSupportActionBar()
                    : null;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mActionBar != null) {
                mActionBar.setTitle(String.format(INTEREST_TITLE_FORMAT, interestNames[position], progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (mActionBar != null) {
                mTitle = mActionBar.getTitle();
                mActionBar.setTitle(String.format(INTEREST_TITLE_FORMAT, interestNames[position], seekBar.getProgress()));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mActionBar != null) {
                mActionBar.setTitle(mTitle);
            }
            interestStorage.setInterest(position, seekBar.getProgress());
            RecommenderService.notifyRecommender(getContext().getApplicationContext());
        }
    }
}
