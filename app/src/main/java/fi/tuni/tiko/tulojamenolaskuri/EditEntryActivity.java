package fi.tuni.tiko.tulojamenolaskuri;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

/**
 * Activity for editing existing entry data and updating or removing the data.
 */
public class EditEntryActivity extends AddNewActivity {

    /**
     * Initialize the view and the attributes inherited from AddNewActivity.
     * @param savedInstanceState    The saved instance state Bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_entry);

        this.saveButton = findViewById(R.id.save);

        setEntryTypeOk(true);
        setSumOk(true);
        setDescriptionOk(true);

        this.enteredSum = findViewById(R.id.enteredSum);
        addListenerForEnteredSum();

        this.dateText = findViewById(R.id.dateText);
        dateText.setText(getFormattedDate());

        this.description = findViewById(R.id.description);
        addListenerForDescription();

        this.radioGroup = findViewById(R.id.radioGroup);
        addListenerForRadioGroup();

        this.dbManager = new DatabaseManager(this);
        setValuesFromDB();
    }

    /**
     * Fetch the item from the database by its id and display the
     * fetched data in the UI.
     */
    public void setValuesFromDB() {
        Long ID = -1L;

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getLong("ID");
        }

        if (ID != -1L) {
            EntryData ed = dbManager.findItemById(ID);

            String description = ed.getDescription();
            String sum = ed.getSum();

            if (sum.startsWith("+")) {
                RadioButton incomeButton = this.radioGroup.findViewById(
                        R.id.income);
                incomeButton.setChecked(true);

            } else if (sum.startsWith("-")) {
                RadioButton expenseButton = this.radioGroup.findViewById(
                        R.id.expense);
                expenseButton.setChecked(true);
            }

            if (sum.startsWith("+") || sum.startsWith("-")) {
                sum = sum.substring(1);
            }

            setDate(ed.getDate());
            this.enteredSum.setText(sum);
            this.dateText.setText(getFormattedDate());
            this.description.setText(description);
        }
    }

    /**
     * Remove the entry with the ID given in the Intent.
     * @param v The remove entry button.
     */
    public void removeEntry(View v) {
        Long ID = -1L;

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getLong("ID");
        }

        if (ID != -1L) {
            showEntryRemovalDialog(ID);
        }
    }

    /**
     * Show the entry removal dialog for the user to decide whether to really
     * remove the entry or cancel.
     * @param ID    The ID of the entry in the database
     */
    public void showEntryRemovalDialog(final Long ID) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,
                R.style.AlertDialogCustomized);
        alertDialog.setTitle("Poista tapahtuma?");

        alertDialog.setPositiveButton("Poista", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteEntry(ID);
                Toast.makeText(EditEntryActivity.this, "Tapahtuma poistettu",
                        Toast.LENGTH_LONG).show();

                int month = getDate().get(Calendar.MONTH);
                int year = getDate().get(Calendar.YEAR);
                moveToMonthlyActivity(year, month);
            }
        });

        alertDialog.setNegativeButton("Peruuta", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Update the entry data and proceed to the given year and month on the
     * monthly view.
     * @param v The save button
     */
    @Override
    public void onSavePressed(View v) {
        Long ID = -1L;

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getLong("ID");
        }

        if (ID != -1L && isSumOk() && isEntryTypeOk() && isDescriptionOk()) {
            updateEntry(ID);
            int month = getDate().get(Calendar.MONTH);
            int year = getDate().get(Calendar.YEAR);
            moveToMonthlyActivity(year, month);
        }
    }

    /**
     * Return to MonthlyActivity when cancel button is pressed.
     * @param v The cancel button
     */
    @Override
    public void onCancelPressed(View v) {
        Intent i = new Intent(this, MonthlyViewActivity.class);
        i.putExtra("month", getDate().get(Calendar.MONTH));
        i.putExtra("year", getDate().get(Calendar.YEAR));
        startActivity(i);
    }

    /**
     * Update an existing entry using the same ID if entry data are changed.
     * @param ID    The id of the entry data in the database
     */
    public void updateEntry(Long ID) {
        Calendar date = getDate();
        String description = this.description.getText().toString();
        String sum = this.enteredSum.getText().toString();

        if (this.entryType.equals("Tulo")) {
            sum = "+" + sum;
        } else if (this.entryType.equals("Meno")) {
            sum = "-" + sum;
        }
        this.dbManager.updateEntry(new EntryData(date, description, sum), ID);

        String text = "Tapahtuma tallennettu";
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
