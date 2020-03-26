package fi.tuni.tiko.tulojamenolaskuri;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class is for showing a DAtePickerDialog fragment.
 */
public class DateSelectorFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener  {

    /**
     * The context that this instance was initialized from
     */
    private Context context;

    /**
     * A TextView that holds the selected or current date
     */
    private TextView dateText;

    /**
     * A DatePicker that holds the selected date
     */
    private DatePicker datePicker;

    /**
     * Constructor that initializes the context and a TextView
     * @param context   The context that this constructor was called from
     * @param dateText  A TextView that holds the date as text
     */
    public DateSelectorFragment(Context context, TextView dateText) {
        this.context = context;
        this.dateText = dateText;
    }

    /**
     * This method can be called to show a date picker dialog.
     */
    public void showDialog() {
        this.show(((AddNewActivity) context).getSupportFragmentManager(),
                "Date Picker");
    }

    /**
     * Returns a date picker dialog with either the current date
     * or a previously selected date if it is created more than once.
     * @param savedInstanceState    The saved instance state
     * @return A new DatePickerDialog with current or previously selected date
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (datePicker != null) {
            year = datePicker.getYear();
            month = datePicker.getMonth();
            day = datePicker.getDayOfMonth();
        }
        return new DatePickerDialog(getActivity(), R.style.CalendarTheme, this,
                year, month, day);
    }

    /**
     * Updates a textView from another activity with the selected date.
     *
     * @param view          A DatePicker
     * @param year          The selected year
     * @param month         The selected month
     * @param dayOfMonth    The selected day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.datePicker = view;
        month += 1;
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        String weekDay = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault());
        String formattedDate = String.format("%s %s.%s.%s", weekDay, dayOfMonth, month, year);
        this.dateText.setText(formattedDate);
    }
}
