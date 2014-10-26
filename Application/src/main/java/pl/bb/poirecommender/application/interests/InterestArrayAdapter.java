package pl.bb.poirecommender.application.interests;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import pl.bb.poirecommender.application.R;
import pl.bb.poirecommender.application.interests.model.Interest;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestArrayAdapter extends ArrayAdapter<Interest> {
    private final Context context;
    private final List<Interest> interestList;
    private final SharedPreferences.Editor interestPrefsEditor;

    public InterestArrayAdapter(Context context, List<Interest> interestList, SharedPreferences.Editor interestPrefsEditor) {
        super(context, R.layout.interest_row, interestList);
        this.context = context;
        this.interestList = interestList;
        this.interestPrefsEditor = interestPrefsEditor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interest_row, null);
        }
        final Interest interest = interestList.get(position);
        ToggleButton activeButton = (ToggleButton) convertView.findViewById(R.id.active_button);
        activeButton.setChecked(interest.isActive());
        activeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                interest.setActive(b);
                interestPrefsEditor.putBoolean(interest.getName(), b);
                interestPrefsEditor.commit();
            }
        });
        TextView interestName = (TextView) convertView.findViewById(R.id.interest_name);
        interestName.setText(interest.getName());
        return convertView;
    }
}
