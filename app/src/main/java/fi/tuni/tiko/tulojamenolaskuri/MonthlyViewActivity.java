package fi.tuni.tiko.tulojamenolaskuri;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MonthlyViewActivity extends AppCompatActivity {

    private RecyclerView monthlyRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyAdapter myAdapter;
    private TextView yearSwitcher;
    private TextView monthSwitcher;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        monthlyRecyclerView = findViewById(R.id.monthlyRecyclerView);

        yearSwitcher = findViewById(R.id.yearSwitcher);
        monthSwitcher = findViewById(R.id.monthSwitcher);

        Calendar currentDate = Calendar.getInstance();
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        String monthText = currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG,
                Locale.getDefault());
        monthText = monthText.substring(0, monthText.length() - 2);
        String yearText = year + "";

        monthSwitcher.setText(monthText);
        yearSwitcher.setText(yearText);

        layoutManager = new LinearLayoutManager(this);
        monthlyRecyclerView.setLayoutManager(layoutManager);
        monthlyRecyclerView.addItemDecoration(new DividerItemDecoration(
                monthlyRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        ArrayList<EntryData> list = new ArrayList<EntryData>();
        Calendar date1 = Calendar.getInstance();
        date1.set(2020, 3, 28);
        list.add(new EntryData(date1,
                "test description", "+255"));
        list.add(new EntryData(date1, "desription", "-257"));
        myAdapter = new MyAdapter(list);
        monthlyRecyclerView.setAdapter(myAdapter);
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
            previousMonth = previousMonth.substring(0, previousMonth.length() - 2);
        }
        monthSwitcher.setText(previousMonth);
    }
}
