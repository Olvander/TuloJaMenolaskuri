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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * This class is for showing add new income or expense entry activity.
 *
 * @author Olli Pertovaara
 * @version 1.7
 * @since   2020.04.20
 */
public class AddNewActivity extends AppCompatActivity {

    /**
     * An instance of DateSelectorFragment class.
     *
     * Used for displaying a Date Picker Dialog.
     */
    protected DateSelectorFragment dateSelector;

    /**
     * A TextView to hold the current or selected date.
     */
    protected TextView dateText;

    /**
     * An EditText for the entered sum.
     */
    protected EditText enteredSum;

    /**
     * An EditText for the description of the entry.
     */
    protected EditText description;

    /**
     * A RadioGroup for the selection of income or expense.
     */
    protected RadioGroup radioGroup;

    /**
     * An instance of Calendar for the set date.
     */
    protected Calendar date;

    /**
     * A boolean for the sum being ok.
     */
    private boolean sumOk;

    /**
     * A boolean for the description being ok.
     */
    private boolean descriptionOk;

    /**
     * A boolean for the entry type (income or expense) being ok.
     */
    private boolean entryTypeOk;

    /**
     * The save button for adding an entry.
     */
    protected Button saveButton;

    /**
     * The entry type as a String.
     */
    protected String entryType;

    /**
     * An instance of the DatabaseManager class.
     */
    protected DatabaseManager dbManager;

    /**
     * An id for the entry data in the database.
     */
    protected Long ID;

    /**
     * Initializes the Add New Entry activity.
     * @param savedInstanceState    The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        this.description = findViewById(R.id.description);
        addListenerForDescription();

        this.radioGroup = findViewById(R.id.radioGroup);
        addListenerForRadioGroup();

        this.dbManager = new DatabaseManager(this);
    }

    /**
     * Adds a listener for the entered sum.
     * The purpose is to set the sum ok if conditions are met.
     */
    public void addListenerForEnteredSum() {
        enteredSum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().contains(".")) {
                    if (v.getText().toString().length() > 2) {
                        setSumOk(true);
                    } else {
                        setSumOk(false);
                    }
                } else {
                    if (v.getText().toString().length() > 0) {
                        setSumOk(true);
                    } else {
                        setSumOk(false);
                    }
                }
                if (isSumOk() && isEntryTypeOk() && isDescriptionOk()) {
                    enableSaveButton();
                } else {
                    disableSaveButton();
                }
                return false;
            }
        });
    }
    /**
     * Adds a listener for the description.
     * The purpose is to set the description ok if its length exceeds 1 char.
     */
    public void addListenerForDescription() {
        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (description.getText().toString().length() > 1) {
                        setDescriptionOk(true);
                    } else {
                        setDescriptionOk(false);
                    }
                if (isSumOk() && isEntryTypeOk() && isDescriptionOk()) {
                    enableSaveButton();
                } else {
                    disableSaveButton();
                }
                return false;
            }
        });
    }

    /**
     * Adds a listener for the income - expense radio group.
     * The purpose is to set the entry type ok on selection.
     */
    public void addListenerForRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setEntryTypeOk(true);
                RadioButton rButton = group.findViewById(checkedId);
                entryType = rButton.getText().toString();
                if (isSumOk() && isDescriptionOk()) {
                    enableSaveButton();
                }
            }
        });
    }

    /**
     * Sets the date
     * @param date  A Calendar instance
     */
    public void setDate(Calendar date) {
        this.date = date;
    }


    /**
     * Getter for the set date as a Calendar instance.
     * @return  The set date
     */
    public Calendar getDate() {
        return this.date;
    }

    /**
     * Sets the sum ok or not ok.
     * @param b Boolean value for the setter
     */
    public void setSumOk(boolean b) {
        this.sumOk = b;
    }

    /**
     * Checks if sum is ok.
     * @return  True if sum has been set
     */
    public boolean isSumOk() {
        return this.sumOk;
    }

    /**
     * Checks if description is ok.
     * @return  True if descirption is set correctly
     */
    public boolean isDescriptionOk() {
        return descriptionOk;
    }

    /**
     * Sets the descipription's state ok or not ok.
     * @param b Boolean value for the setter
     */
    public void setDescriptionOk(boolean b) {
        this.descriptionOk = b;
    }

    /**
     * Checks if entry types is ok
     * @return  True if entry type is selected
     */
    public boolean isEntryTypeOk() {
        return entryTypeOk;
    }

    /**
     * Setter for if the entry type is ok.
     * @param entryTypeOk   Boolean value for entry type being ok
     */
    public void setEntryTypeOk(boolean entryTypeOk) {
        this.entryTypeOk = entryTypeOk;
    }

    /**
     * Enables the save button
     */
    public void enableSaveButton() {
        saveButton.setEnabled(true);
        saveButton.setBackgroundColor(Color.parseColor("#0075c0"));
    }

    /**
     * Disables the save button.
     */
    public void disableSaveButton() {
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(Color.parseColor("#78a4bf"));
    }

    /**
     * Return the formatted current date or date fetched from db.
     * @return The formatted date
     */
    public String getFormattedDate() {

        Calendar c = GregorianCalendar.getInstance();

        if (this.date != null) {
            c = this.date;
        }
        setDate(c);

        String weekDayText = c.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        String formattedDate = String.format("%s %s.%s.%s", weekDayText, day,
                month, year);

        return formattedDate;
    }

    /**
     * Starts the MainActivity when cancel is pressed.
     * @param v The cancel button
     */
    public void onCancelPressed(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Gathers and saves the entry data to the database, shows a toast
     * about a succesful save and proceeds to monthly activity.
     * @param v The save button
     */
    public void onSavePressed(View v) {

        if (isSumOk() && isEntryTypeOk() && isDescriptionOk()) {
            Calendar date = getDate();
            String description = this.description.getText().toString();
            String sum = this.enteredSum.getText().toString();

            if (entryType.equals("Tulo")) {
                sum = "+" + sum;
            } else if (entryType.equals("Meno")) {
                sum = "-" + sum;
            }

            this.dbManager.saveEntry(new EntryData(date, description, sum));

            String text = "Tapahtuma tallennettu";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            int month = getDate().get(Calendar.MONTH);
            int year = getDate().get(Calendar.YEAR);
            moveToMonthlyActivity(year, month);
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

    /**
     * Proceeds to monthly activity.
     * @param year  The year which should be displayed in the monthly activity
     * @param month The month that should be displayed in the monthly activity
     */
    public void moveToMonthlyActivity(int year, int month) {
        Intent i = new Intent(this, MonthlyViewActivity.class);
        i.putExtra("year", year);
        i.putExtra("month", month);
        startActivity(i);
    }
}
