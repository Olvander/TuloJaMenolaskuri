package fi.tuni.tiko.tulojamenolaskuri;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 *  For displaying the monthly entry data.
 *
 * @author Olli Pertovaara
 * @version 1.7
 * @since   2020.04.20
 */
public class MonthlyViewActivity extends AppCompatActivity {

    /**
     *  The monthly recycler view.
     */
    private RecyclerView monthlyRecyclerView;

    /**
     *  A LinearLayoutManager used to display the view.
     */
    private LinearLayoutManager layoutManager;

    /**
     *  An instance of the MyAdapter custom class.
     */
    private MyAdapter myAdapter;

    /**
     *  The year text switcher view.
     */
    private TextView yearSwitcher;

    /**
     *  The month text switcher view.
     */
    private TextView monthSwitcher;

    /**
     *  The monthly total sum TextView.
     */
    private TextView monthlyTotal;

    /**
     *  The month to display the data for.
     */
    private int month;

    /**
     *  The year to display the data for.
     */
    private int year;

    /**
     *  An instance of the DatabaseManager class.
     */
    private DatabaseManager dbManager;

    /**
     *  The menu in the action bar.
     */
    private Menu menu;

    /**
     *  For initialization of variables and views.
     *
     * @param savedInstanceState    The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        monthlyRecyclerView = findViewById(R.id.monthlyRecyclerView);

        yearSwitcher = findViewById(R.id.yearSwitcher);
        monthSwitcher = findViewById(R.id.monthSwitcher);
        monthlyTotal = findViewById(R.id.monthlyTotal);

        Calendar currentDate = Calendar.getInstance();

        if (getIntent().getExtras() != null) {
            int monthToReturnTo = getIntent().getExtras().getInt("month");
            int yearToReturnTo = getIntent().getExtras().getInt("year");

            currentDate.set(Calendar.MONTH, monthToReturnTo);
            currentDate.set(Calendar.YEAR, yearToReturnTo);
        }

        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        String monthText = currentDate.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.getDefault());
        monthText = monthText.substring(0, monthText.length() - 2);
        String yearText = year + "";

        monthSwitcher.setText(monthText);
        yearSwitcher.setText(yearText);

        layoutManager = new LinearLayoutManager(this);
        monthlyRecyclerView.setLayoutManager(layoutManager);
        monthlyRecyclerView.addItemDecoration(new DividerItemDecoration(
                monthlyRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        myAdapter = new MyAdapter(this, fetchMonthlyEntries(), year, month + 1);
        monthlyRecyclerView.setAdapter(myAdapter);

        setMonthlyTotal();
        addTextChangeListenerOn(monthSwitcher);
        addTextChangeListenerOn(yearSwitcher);
    }

    /**
     * Sets the monthly total text and adds a + if it is a positive sum.
     */
    public void setMonthlyTotal() {
        double sum = dbManager.getSumOfMonthlyItems(year, month + 1);
        String sumAsText = "" + sum;

        if (sum > 0) {
            sumAsText = "Yhteensä: +" + sum + " €";
        } else {
            sumAsText = "Yhteensä: " + sum + " €";
        }
        monthlyTotal.setText(sumAsText);
        setMonthlyTotalColor(sumAsText);
    }

    /**
     * Sets the monthly total color as green if it is 0 or more, red if less.
     * @param sumAsText The text whose color should be set.
     */
    public void setMonthlyTotalColor(String sumAsText) {
        if (sumAsText.contains("+")) {
            monthlyTotal.setTextColor(Color.parseColor("#99cc00"));
        } else if (sumAsText.contains("-")) {
            monthlyTotal.setTextColor(Color.parseColor("#ce2222"));
        } else {
            monthlyTotal.setTextColor(Color.parseColor("#99cc00"));
        }
    }

    /**
     * Adds a text change listener on a text view and updates the view after
     * text has changed.
     *
     * @param tv The TextView that should be updated
     */
    public void addTextChangeListenerOn(TextView tv) {
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                myAdapter = new MyAdapter(MonthlyViewActivity.this,
                        fetchMonthlyEntries(), year, month + 1);
                monthlyRecyclerView.setAdapter(myAdapter);
                setMonthlyTotal();
            }
        });
    }

    public void addListenerOnYearSwitcher() {
        myAdapter = new MyAdapter(this, fetchMonthlyEntries(), year, month + 1);
        monthlyRecyclerView.setAdapter(myAdapter);
    }

    /**
     * Adds entry data to a list after fetching the data from the database.
     *
     * @return  The entry list
     */
    public ArrayList<EntryData> fetchMonthlyEntries() {
        dbManager = new DatabaseManager(this);
        ArrayList<EntryData> entryList = new ArrayList<EntryData>();
        entryList = dbManager.getAllByMonthAndYear(year, month + 1);
        return entryList;
    }

    /**
     * Adds 1 more year to the current year and sets it as the year title.
     *
     * @param v The button for the next year that was pressed
     */
    public void nextYearPressed(View v) {
        this.year += 1;
        String nextYear = this.year + "";
        yearSwitcher.setText(nextYear);
    }

    /**
     * Subtracts the current year by one and sets it as the year title
     *
     * @param v The button for the previous year that was pressed
     */
    public void previousYearPressed(View v) {
        this.year -= 1;
        String previousYear = this.year + "";
        yearSwitcher.setText(previousYear);
    }

    /**
     * Updates the view with the next month's entry data and month title.
     * Removes the last two letters from the long version of the month name
     * to get the proper month name.
     *
     * @param v The next month's button
     */
    public void nextMonthPressed(View v) {
        if (this.month == 11) {
            this.month = 0;
        } else {
            this.month += 1;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, this.month);
        String nextMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG,
                Locale.getDefault());
        if (this.month == 1) {
            nextMonth = "helmikuu";
        } else {
            nextMonth = nextMonth.substring(0, nextMonth.length() - 2);
        }
        monthSwitcher.setText(nextMonth);
    }

    /**
     * Updates the view with the previous month's entry data and month title.
     * Removes the last two letters from the long version of the month name
     * to get the proper month name.
     *
     * @param v The previous month's button
     */
    public void previousMonthPressed(View v) {
        if (this.month == 0) {
            this.month = 11;
        } else {
            this.month -= 1;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, this.month);
        String previousMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG,
                Locale.getDefault());

        if (this.month == 1) {
            previousMonth = "helmikuu";
        } else {
            previousMonth = previousMonth.substring(0,
                    previousMonth.length() - 2);
        }
        monthSwitcher.setText(previousMonth);
    }

    /**
     * On RecyclerView row press, proceed to Edit Entry activity.
     * @param v The View that was pressed
     */
    public void proceedToEditEntry(View v) {
        int position = monthlyRecyclerView.getChildViewHolder(v)
                .getAdapterPosition();
        Long ID = myAdapter.getItemId(position);
        Intent i = new Intent(this, EditEntryActivity.class);
        i.putExtra("ID", ID);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * The user will return from MonthlyViewActivity to MainAcitivity
     * as back button is pressed.
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.monthly_menu, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        MenuItem removeMonthsData = this.menu.findItem(R.id.removeMonthsData);

        if (item == removeMonthsData) {

            showMonthlyDataRemovalDialog();
        }
        return item == removeMonthsData;
    }

    /**
     * Show the monthly data removal dialog for the user to decide whether to
     * really remove the data or cancel. If user selects ok, removes current
     * month's data.
     */
    public void showMonthlyDataRemovalDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,
                R.style.AlertDialogCustomized);
        alertDialog.setTitle("Poista kaikki kuukauden tiedot?");

        alertDialog.setPositiveButton("Poista", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MonthlyViewActivity.this, "Tiedot poistettu",
                        Toast.LENGTH_LONG).show();

                removeCurrentMonthsData();
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
     * Utilizes the DatabaseManager class to delete entries one by one based on
     * currently displayed year. Also updates the view.
     */
    public void removeCurrentMonthsData() {
        long id = -1L;

        Cursor c = dbManager.getCursorForMonthAndYear(year, month + 1);
        if (c != null) {
            c.moveToFirst();
            id = c.getLong(0);

            if (id >= 0L) {
                dbManager.deleteEntry(id);
            }

            while (c.moveToNext()) {
                id = c.getLong(0);

                if (id >= 0L) {
                    dbManager.deleteEntry(id);
                }
            }
            myAdapter = new MyAdapter(this, fetchMonthlyEntries(), year, month + 1);
            monthlyRecyclerView.setAdapter(myAdapter);

            setMonthlyTotal();
        }
    }
}
