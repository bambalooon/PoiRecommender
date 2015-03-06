package pl.edu.agh.eis.poirecommender.application.rules;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import heart.alsvfd.Formulae;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartPreferences;

import java.util.LinkedList;

/**
 * Created by Krzysztof Balon on 2014-11-16.
 */
public class RulesFragment extends ListFragment {
    private RuleArrayAdapter ruleArrayAdapter;
    private HeartPreferences heartPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Context context = getActivity().getApplicationContext();
        ruleArrayAdapter = new RuleArrayAdapter(getActivity()); //TODO: leakage possible?
        heartPreferences = new HeartPreferences(context);
        setListAdapter(ruleArrayAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rule_modification, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                XTTModel temporaryXttModel = heartPreferences.getTemporaryXttModel();
                Optional<Table> table = FluentIterable.from(temporaryXttModel.getTables())
                        .filter(new Predicate<Table>() {
                            @Override
                            public boolean apply(Table input) {
                                return HeartManager.RECOMMENDATIONS_TABLE_NAME.equals(input.getName());
                            }
                        }).first();
                if(table.isPresent()) {
                    LinkedList<Rule> rules = table.get().getRules();
                    Rule baseRule = rules.getLast();

                    Rule.Builder builder = new Rule.Builder();
                    Rule.Builder.IncompleteRuleId incompleteRuleId = new Rule.Builder.IncompleteRuleId();
                    incompleteRuleId.orderNumber = 0;
                    builder.setRuleId(incompleteRuleId);

                    for (Formulae formulae : baseRule.getConditions()) {
                        builder.addCondition(new Formulae.Builder()
                                .setAttribute(formulae.getAttribute())
                                .setOp(formulae.getOp())
                                .setValue(formulae.getValue()));
                    }

                    Decision decision = baseRule.getDecisions().getFirst();
                    builder.addDecision(new Decision.Builder()
                            .setAttribute(decision.getAttribute())
                            .setDecision(decision.getDecision()));
                    rules.add(builder.build());
                    ruleArrayAdapter.updateRuleList(rules);
                    ruleArrayAdapter.notifyDataSetChanged();
                    new TemporaryXttModelSaveTask().execute(temporaryXttModel);
                }
                return true;
            case R.id.action_cancel:
                XTTModel xttModel = heartPreferences.getXttModel();
                Optional<Table> tableOp = FluentIterable.from(xttModel.getTables())
                        .filter(new Predicate<Table>() {
                            @Override
                            public boolean apply(Table input) {
                                return HeartManager.RECOMMENDATIONS_TABLE_NAME.equals(input.getName());
                            }
                        }).first();
                if(tableOp.isPresent()) {
                    LinkedList<Rule> rules = tableOp.get().getRules();
                    ruleArrayAdapter.updateRuleList(rules);
                    ruleArrayAdapter.notifyDataSetChanged();
                }
                new TemporaryXttModelSaveTask().execute(xttModel);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TemporaryXttModelSaveTask extends AsyncTask<XTTModel, Void, Void> {

        @Override
        protected Void doInBackground(XTTModel... xttModels) {
            heartPreferences.setTemporaryXttModel(xttModels[0]);
            return null;
        }
    }
}
