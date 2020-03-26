package fi.tuni.tiko.tulojamenolaskuri;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * This class is for showing add new income or expense event activity.
 */
public class AddNewActivity extends AppCompatActivity {

    /**
     * An instance of DateSelectorFragment class.
     *
     * Used for displaying a Date Picker Dialog.
     */
    private DateSelectorFragment dateSelector;

    /**
     * A TextView to hold the current or selected date.
     */
    private TextView dateText;

    private EditText enteredSum;

    private RadioGroup radioGroup;

    private Calendar date;

    private boolean sumOk;

    private boolean entryTypeOk;

    private Button saveButton;

    private String entryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        this.saveButton = findViewById(R.id.save);

        disableSaveButton();
        setEntryTypeOk(false);
        setSumOk(false);

        this.enteredSum = findViewById(R.id.enteredSum);
        addListenerForEnteredSum();

        this.dateText = findViewById(R.id.dateText);
        dateText.setText(getFormattedDate());

        this.radioGroup = findViewById(R.id.radioGroup);
        addListenerForRadioGroup();
    }

    public void addListenerForEnteredSum() {
        enteredSum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().contains(".")) {
                    if (v.getText().length() > 2) {
                        setSumOk(true);
                    } else {
                        setSumOk(false);
                    }
                } else {
                    if (v.getText().length() > 0) {
                        setSumOk(true);
                    } else {
                        setSumOk(false);
                    }
                }
                if (isSumOk() && isEntryTypeOk()) {
                    enableSaveButton();
                } else {
                    disableSaveButton();
                }
                return false;
            }
        });
    }

    public void addListenerForRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setEntryTypeOk(true);
                RadioButton rButton = group.findViewById(checkedId);
                entryType = rButton.getText().toString();
                if (isSumOk()) {
                    enableSaveButton();
                }
            }
        });
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setSumOk(boolean b) {
        this.sumOk = b;
    }

    public boolean isSumOk() {
        return this.sumOk;
    }

    public boolean isEntryTypeOk() {
        return entryTypeOk;
    }

    public void setEntryTypeOk(boolean entryTypeOk) {
        this.entryTypeOk = entryTypeOk;
    }

    public void enableSaveButton() {
        saveButton.setEnabled(true);
        saveButton.setBackgroundColor(Color.parseColor("#0075c0"));
    }

    public void disableSaveButton() {
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(Color.parseColor("#78a4bf"));
    }

    /**
     * Return the formatted current date.
     * @return The formatted date
     */
    public String getFormattedDate() {

        Calendar c = GregorianCalendar.getInstance();
        setDate(c);
        String weekDayText = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        String formattedDate = String.format("%s %s.%s.%s", weekDayText, day, month, year);

        return formattedDate;
    }

    public void onCancelPressed(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onSavePressed(View v) {

        if (isSumOk() && isEntryTypeOk()) {
            String text = "Tapahtuma tallennettu";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show DatePickerDialog when Choose date has been pressed
     * @param v The view whose onclick method was called
     */
    public void onSetDatePressed(View v) {
        if (dateSelector == null) {
            dateSelector = new DateSelectorFragment(this, dateText);
        }
        dateSelector.showDialog();
    }
}
