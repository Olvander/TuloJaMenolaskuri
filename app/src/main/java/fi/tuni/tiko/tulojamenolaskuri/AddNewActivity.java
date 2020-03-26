package fi.tuni.tiko.tulojamenolaskuri;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        TextView dateText = findViewById(R.id.dateText);
        this.dateText = dateText;
        dateText.setText(getFormattedDate());
    }

    /**
     * Return the formatted current date.
     * @return The formatted date
     */
    public String getFormattedDate() {

        Calendar c = GregorianCalendar.getInstance();
        String weekDayText = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        String formattedDate = String.format("%s %s.%s.%s", weekDayText, day, month, year);

        return formattedDate;
    }

    public void onCancelPressed(View v) {

    }

    public void onSavePressed(View v) {

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
