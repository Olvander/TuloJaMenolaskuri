package fi.tuni.tiko.tulojamenolaskuri;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MonthlyViewActivity extends AppCompatActivity {

    private RecyclerView monthlyRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyAdapter myAdapter;
    private TextView yearSwitcher;
    private TextView monthSwitcher;
    private TextView monthlyTotal;
    private int month;
    private int year;
    private DatabaseManager dbManager;

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

    public void setMonthlyTotalColor(String sumAsText) {
        if (sumAsText.contains("+")) {
            monthlyTotal.setTextColor(Color.parseColor("#99cc00"));
        } else if (sumAsText.contains("-")) {
            monthlyTotal.setTextColor(Color.parseColor("#ce2222"));
        } else {
            monthlyTotal.setTextColor(Color.parseColor("#99cc00"));
        }
    }

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

    public ArrayList<EntryData> fetchMonthlyEntries() {
        dbManager = new DatabaseManager(this);
        ArrayList<EntryData> entryList = new ArrayList<EntryData>();
        entryList = dbManager.getAllByMonthAndYear(year, month + 1);
        return entryList;
    }

    public void nextYearPressed(View v) {
        this.year += 1;
        String nextYear = this.year + "";
        yearSwitcher.setText(nextYear);
    }

    public void previousYearPressed(View v) {
        this.year -= 1;
        String previousYear = this.year + "";
        yearSwitcher.setText(previousYear);
    }

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
}
