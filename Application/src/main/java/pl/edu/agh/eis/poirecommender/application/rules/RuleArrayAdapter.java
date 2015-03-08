package pl.edu.agh.eis.poirecommender.application.rules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import heart.alsvfd.*;
import heart.exceptions.RangeFormatException;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartPreferences;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.TimeHourAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-16.
 */
public class RuleArrayAdapter extends ArrayAdapter<Rule> {
    private static final int[] ROW_BACKGROUND_COLORS = {Color.TRANSPARENT, Color.YELLOW};
    private static final List<String> SINGLE_VALUE_OPERATIONS = ImmutableList.of("eq", "neq", "lt", "gt", "lte", "gte");
    private static final String ANY_ELEMENT = "any";
    private final HeartPreferences heartPreferences;
    private XTTModel temporaryXttModel;
    private List<Rule> ruleList;

    public RuleArrayAdapter(Activity activity, HeartPreferences heartPreferences) {
        super(activity, R.layout.rule_row);
        this.heartPreferences = heartPreferences;
        updateRuleList();
    }

    @Override
    public int getCount() {
        return ruleList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO: clean up, optimize, create classes encapsulating rule menu creation
        final LayoutInflater inflater = LayoutInflater.from(getContext());
//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rule_row, parent, false);
            convertView.setBackgroundColor(ROW_BACKGROUND_COLORS[position % ROW_BACKGROUND_COLORS.length]);
//        }
        final Rule rule = ruleList.get(position);
        LinearLayout ruleRow = (LinearLayout) convertView;
        for (final Formulae formulae : rule.getConditions()) {
            final String attributeName = formulae.getAttribute().getName();
            final String operationName = formulae.getOp().getText();
            final String attributeValue = formulae.getValue().toString();

            RelativeLayout conditionRow = (RelativeLayout) inflater.inflate(R.layout.rule_condition_row, ruleRow, false);
            TextView attributeNameText = (TextView) conditionRow.findViewById(R.id.attribute_name);
            attributeNameText.setText(attributeName);

            final Spinner attributeOpSpinner = (Spinner) conditionRow.findViewById(R.id.attribute_operation);
            ArrayAdapter<CharSequence> operationAdapter = ArrayAdapter.createFromResource(
                    getContext(),
                    TimeHourAdapter.HOUR_ATTRIBUTE.equals(attributeName)
                            ? R.array.operation_numeric_array
                            : R.array.operation_symbolic_array,
                    android.R.layout.simple_spinner_item);
            operationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            attributeOpSpinner.setAdapter(operationAdapter);
            attributeOpSpinner.setSelection(operationAdapter.getPosition(operationName));
            attributeOpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedOperator = (String) adapterView.getItemAtPosition(i);
                    formulae.setOp(Formulae.ConditionalOperator.fromString(selectedOperator));
                    new TemporaryXttModelSaveTask().execute();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            TextView attributeValueText = (TextView) conditionRow.findViewById(R.id.attribute_value);
            attributeValueText.setText(attributeValue);
            attributeValueText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final List<Value> originalItems = formulae.getAttribute().getType().getDomain().getValues();
                    FluentIterable<String> itemsTemp = FluentIterable
                            .from(originalItems)
                            .transform(new Function<Value, String>() {
                                @Override
                                public String apply(Value input) {
                                    return input.toString();
                                }
                            });
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    String selectedOperation = (String) attributeOpSpinner.getSelectedItem();
                    Value originalValue = formulae.getValue();
                    boolean singleValueOperation = SINGLE_VALUE_OPERATIONS.contains(selectedOperation);
                    if(TimeHourAdapter.HOUR_ATTRIBUTE.equals(attributeName)) {
                        if(singleValueOperation) {
                            builder.setTitle(R.string.enter_number_dialog);
                            final EditText inputField = new EditText(getContext());
                            if (originalValue instanceof SimpleNumeric) {
                                SimpleNumeric value = (SimpleNumeric) originalValue;
                                inputField.setText(value.getValue().toString());
                            } else if (originalValue instanceof Any) {
                                inputField.setText(ANY_ELEMENT);
                            }
                            builder.setView(inputField);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String value = inputField.getText().toString();
                                    if(ANY_ELEMENT.equals(value)) {
                                        formulae.setValue(new Any());
                                    } else {
                                        formulae.setValue(new SimpleNumeric(Double.parseDouble(value)));
                                    }
                                    notifyDataSetChanged();
                                    new TemporaryXttModelSaveTask().execute();
                                }
                            });
                        } else {
                            builder.setTitle(R.string.enter_range_dialog);
                            final EditText inputField = new EditText(getContext());
                            if (originalValue instanceof SetValue) {
                                SetValue value = (SetValue) originalValue;
                                inputField.setText(value.getValues().toString());
                            }
                            builder.setView(inputField);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String value = inputField.getText().toString().trim();
                                    if(!value.startsWith("[") || !value.endsWith("]"))
                                        return;
                                    value = value.substring(1, value.length()-1);
                                    String[] values = value.split(",");
                                    SetValue setValue = new SetValue();
                                    for (String val : values) {
                                        val = val.trim();
                                        if(val.startsWith("<") && val.endsWith(">")) {
                                            val = val.substring(1, val.length()-1);
                                            String[] rangeValues = val.split(";");
                                            SimpleNumeric fromNumeric = new SimpleNumeric(Double.valueOf(rangeValues[0].trim()));
                                            SimpleNumeric toNumeric = new SimpleNumeric(Double.valueOf(rangeValues[1].trim()));
                                            try {
                                                Range range = new Range(fromNumeric, toNumeric);
                                                setValue.appendValue(range);
                                            } catch (RangeFormatException e) {
                                                return;
                                            }
                                        } else {
                                            SimpleNumeric numeric = new SimpleNumeric(Double.valueOf(val));
                                            setValue.appendValue(numeric);
                                        }
                                    }
                                    formulae.setValue(setValue);
                                    notifyDataSetChanged();
                                    new TemporaryXttModelSaveTask().execute();
                                }
                            });
                        }
                    } else {
                        if (singleValueOperation) {
                            String[] items = itemsTemp
                                    .append(ANY_ELEMENT)
                                    .toArray(String.class);
                            int selectedItemIndex;
                            if (originalValue instanceof SimpleSymbolic) {
                                SimpleSymbolic value = (SimpleSymbolic) originalValue;
                                selectedItemIndex = originalItems.indexOf(value);
                            } else if (originalValue instanceof Any) {
                                selectedItemIndex = originalItems.size();
                            } else if (originalValue instanceof SetValue) {
                                List<Value> value = ((SetValue) originalValue).getValues();
                                selectedItemIndex = 0;
                                for (Value originalItem : originalItems) {
                                    if (originalItem.toString().equals(value.get(0).toString())) {
                                        break;
                                    }
                                    selectedItemIndex++;
                                }
                            } else {
                                throw new IllegalArgumentException("Unexpected type of Value");
                            }
                            builder.setTitle(R.string.select_single_item_dialog);
                            builder.setSingleChoiceItems(items, selectedItemIndex, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final Value newValue =
                                            i == originalItems.size()
                                                    ? new Any()
                                                    : originalItems.get(i);
                                    formulae.setValue(newValue);
                                    notifyDataSetChanged();
                                    new TemporaryXttModelSaveTask().execute();
                                }
                            });
                        } else {
                            final List<Value> values;
                            if(originalValue instanceof SimpleSymbolic) {
                                values = Lists.newArrayList(originalValue);
                            } else {
                                values = ((SetValue) originalValue).getValues();
                            }
                            boolean[] selectedItems = Booleans.toArray(
                                    FluentIterable.from(originalItems)
                                            .transform(new Function<Value, Boolean>() {
                                                @Override
                                                public Boolean apply(final Value originalItem) {
                                                    return FluentIterable.from(values)
                                                            .anyMatch(new Predicate<Value>() {
                                                                @Override
                                                                public boolean apply(Value selectedItem) {
                                                                    return originalItem.toString().equals(selectedItem.toString());
                                                                }
                                                            });
                                                }
                                            }).toList());
                            String[] items = itemsTemp.toArray(String.class);
                            builder.setTitle(R.string.select_items_dialog);
                            builder.setMultiChoiceItems(items, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    final Value modifiedItem = originalItems.get(i);
                                    List<Value> selectedItems;
                                    if(formulae.getValue() instanceof SimpleSymbolic) {
                                        SetValue setValue = new SetValue();
                                        selectedItems = Lists.newArrayList(formulae.getValue());
                                        setValue.setValues(selectedItems);
                                        formulae.setValue(setValue);
                                    } else {
                                        selectedItems = ((SetValue) formulae.getValue()).getValues();
                                    }
                                    if (b) {
                                        selectedItems.add(modifiedItem);
                                    } else {
                                        Optional<Value> itemToRemove = FluentIterable.from(selectedItems)
                                                .filter(new Predicate<Value>() {
                                                    @Override
                                                    public boolean apply(Value input) {
                                                        return input.toString().equals(modifiedItem.toString());
                                                    }
                                                }).first();
                                        if (itemToRemove.isPresent()) {
                                            selectedItems.remove(itemToRemove.get());
                                        }
                                    }
                                    notifyDataSetChanged();
                                    new TemporaryXttModelSaveTask().execute();
                                }
                            });
                        }
                        builder.setPositiveButton(R.string.ok, null);
                    }
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            ruleRow.addView(conditionRow);
        }
        for (final Decision decision : rule.getDecisions()) {
            RelativeLayout decisionRow = (RelativeLayout) inflater.inflate(R.layout.rule_decision_row, ruleRow, false);
            TextView attributeNameText = (TextView) decisionRow.findViewById(R.id.attribute_name);
            attributeNameText.setText(decision.getAttribute().getName());
            TextView attributeValueText = (TextView) decisionRow.findViewById(R.id.attribute_value);
            attributeValueText.setText(decision.getDecision().toString());
            attributeValueText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.select_single_item_dialog);
                    final List<Value> originalItems = decision.getAttribute().getType().getDomain().getValues();
                    String[] items = FluentIterable
                            .from(originalItems)
                            .transform(new Function<Value, String>() {
                                @Override
                                public String apply(Value input) {
                                    return input.toString();
                                }
                            }).toArray(String.class);
                    SimpleSymbolic originalValue = (SimpleSymbolic) decision.getDecision();
                    int selectedItemIndex = 0;
                    for (Value originalItem : originalItems) {
                        if (originalItem.toString().equals(originalValue.toString())) {
                            break;
                        }
                        selectedItemIndex++;
                    }
                    builder.setSingleChoiceItems(items, selectedItemIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Value newValue = originalItems.get(i);
                            decision.setDecision(newValue);
                            notifyDataSetChanged();
                            new TemporaryXttModelSaveTask().execute();
                        }
                    });
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            ruleRow.addView(decisionRow);
        }
        return convertView;
    }

    public void updateRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    private void updateRuleList() {
        if(ruleList == null) {
            this.temporaryXttModel = heartPreferences.getTemporaryXttModel();
            if (temporaryXttModel == null) {
                temporaryXttModel = heartPreferences.getXttModel();
                heartPreferences.setTemporaryXttModel(temporaryXttModel);
            }
            List<Table> tables = FluentIterable.from(temporaryXttModel.getTables())
                    .filter(new Predicate<Table>() {
                        @Override
                        public boolean apply(Table input) {
                            return HeartManager.RECOMMENDATIONS_TABLE_NAME.equals(input.getName());
                        }
                    }).toList();
            switch (tables.size()) {
                case 0:
                    this.ruleList = Collections.emptyList();
                    break;
                case 1:
                    this.ruleList = tables.get(0).getRules();
                    break;
                default:
                    throw new IllegalStateException("Expected single Recommendations table in XTT Model, but found more.");
            }
        }
    }

    private class TemporaryXttModelSaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            heartPreferences.setTemporaryXttModel(temporaryXttModel);
            return null;
        }
    }
}
