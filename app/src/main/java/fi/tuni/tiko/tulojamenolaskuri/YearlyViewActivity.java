package fi.tuni.tiko.tulojamenolaskuri;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Used for displaying the yearly view for the income and expenses entry data.
 *
 * @author Olli Pertovaara
 * @version 1.7
 * @since   2020.04.20
 */
public class YearlyViewActivity extends AppCompatActivity implements Runnable {

    /**
     * A DatabaseManager class instance.
     */
    private DatabaseManager dbManager;

    /**
     *  An arraylist for the sums to be displayed.
     */
    private ArrayList<Double> sums;

    /**
     *  An arraylist for the month views to be displayed.
     */
    private ArrayList<View> months;

    /**
     *  An arraylist for the text views that hold the monthly sums.
     */
    private ArrayList<TextView> monthlySums;

    /**
     *  The maximum value of the displayed monthly sums.
     */
    private Double max;

    /**
     *  The minimum value of the displayed monthly sums.
     */
    private Double min;

    /**
     *  The year switcher text view that holds the year to be displayed.
     */
    private TextView yearSwitcher;

    /**
     *  The year whose data to display.
     */
    private int year;

    /**
     *  The linear layout that this activity's layout is made with
     */
    private LinearLayout layout;

    /**
     *  The menu for the removal options.
     */
    private Menu menu;

    /**
     *  For initializing of the view.
     *
     * @param savedInstanceState    The saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        months = new ArrayList<View>();

        monthlySums = new ArrayList<>();

        dbManager = new DatabaseManager(this);

        setYear();
        iterateSums();

        //get bounds
        setContentView(R.layout.activity_yearly);

        yearSwitcher = findViewById(R.id.yearSwitcher);
        String yearText = year + "";
        yearSwitcher.setText(yearText);
        addTextChangeListenerOn(yearSwitcher);

        // initializeTheMonthsList()
        months.add(findViewById(R.id.january));
        months.add(findViewById(R.id.february));
        months.add(findViewById(R.id.march));
        months.add(findViewById(R.id.april));
        months.add(findViewById(R.id.may));
        months.add(findViewById(R.id.june));
        months.add(findViewById(R.id.july));
        months.add(findViewById(R.id.august));
        months.add(findViewById(R.id.september));
        months.add(findViewById(R.id.october));
        months.add(findViewById(R.id.november));
        months.add(findViewById(R.id.december));

        layout = findViewById(R.id.barGraphView);

        // initializeMonthlySumsList()
        monthlySums.add((TextView) findViewById(R.id.janSum));
        monthlySums.add((TextView) findViewById(R.id.febSum));
        monthlySums.add((TextView) findViewById(R.id.marSum));
        monthlySums.add((TextView) findViewById(R.id.aprSum));
        monthlySums.add((TextView) findViewById(R.id.maySum));
        monthlySums.add((TextView) findViewById(R.id.junSum));
        monthlySums.add((TextView) findViewById(R.id.julSum));
        monthlySums.add((TextView) findViewById(R.id.augSum));
        monthlySums.add((TextView) findViewById(R.id.sepSum));
        monthlySums.add((TextView) findViewById(R.id.octSum));
        monthlySums.add((TextView) findViewById(R.id.novSum));
        monthlySums.add((TextView) findViewById(R.id.decSum));

        layout.post(this);

        /*
        View barChartBase = findViewById(R.id.barChartBase);
        BarDrawableView bar1 = new BarDrawableView(this, null);
//        bar1.onDraw(new Canvas(barChartBase));

        barChartBase.setBackground(bar1.getBarDrawable());
        bar1.invalidate();
        //Canvas canvas = new Canvas();
        //bar1.draw(canvas);

        //setContentView(bar1);
*/
    }

    /**
     * Set the year to be displayed as the current year.
     */
    public void setYear() {
        Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
    }

    /**
     *  Adds sums to the sums arraylist and defines the min and max values.
     */
    public void iterateSums() {
        max = 0.0;
        min = 0.0;

        sums = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            double sum = dbManager.getSumOfMonthlyItems(year, month);

            sums.add(sum);

            if (month == 1) {
                min = sum;
                max = sum;
            } else {
                if (sum < min) {
                    min = sum;
                }
                if (sum > max) {
                    max = sum;
                }
            }
        }
    }

    /**
     *  Meant for creating the positive and negative bars for the sum values.
     *
     *  Meant to be used after the view has been initialized.
     */
    public void run() {
        float parentViewHeight = findViewById(R.id.barGraphView).getHeight();
        float zeroPoint = parentViewHeight / 2;

        int i = 0;
        for (Double sum : sums) {

            View bar = months.get(i);

            int barHeight = 0;
            if (Math.abs(max) > Math.abs(min)) {
                barHeight = (int) Math.round((Math.abs(sum) / Math.abs(max)) * zeroPoint);
            } else {
                barHeight = (int) Math.round((Math.abs(sum) / Math.abs(min)) * zeroPoint);
            }

            if (barHeight < 2 && sum != 0) {
                barHeight = 2;
            }

            if (sum < 0) {
                createNegativeBar(bar, barHeight, zeroPoint);
            } else if (sum >= 0) {
                createPositiveBar(bar, barHeight, zeroPoint);
            }
            setMonthlySum(sum, i);
            i++;
        }
        /*
        createPositiveBar(months.get(0), (int) ((25f / 100f) * zeroPoint), zeroPoint);
        createPositiveBar(months.get(1), (int) ((75f / 100f) * zeroPoint), zeroPoint);

        createNegativeBar(months.get(2), (int) ((90.0f / 100.0f) * zeroPoint), zeroPoint);

         */
    }

    /**
     *  Fills a view with a color to represent a positive sum value
     *
     * @param v         The view that should be filled with a color
     * @param barHeight The height of the bar
     * @param zeroPoint The zero point position
     */
    public void createPositiveBar(View v, int barHeight, float zeroPoint) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = barHeight;
        v.setLayoutParams(layoutParams);
        v.setTranslationY(zeroPoint - (float) barHeight);
        v.setBackgroundColor(Color.parseColor("#99cc00"));
    }

    /**
     *  Fills a view with a color to represent a negative sum value
     *
     * @param v         The view that should be filled with a color
     * @param barHeight The height of the bar
     * @param zeroPoint The zero point position
     */
    public void createNegativeBar(View v, int barHeight, float zeroPoint) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = barHeight;
        v.setLayoutParams(layoutParams);
        v.setTranslationY(zeroPoint);
        v.setBackgroundColor(Color.parseColor("#c50000"));
    }

    /**
     * Sets the monthly sum underneath each bar
     *
     * @param sum   The sum
     * @param month The month whose value the sum is
     */
    public void setMonthlySum(double sum, int month) {
        TextView tv = monthlySums.get(month);
        String sumText = "";
        if (sum >= 0) {
            sumText = "+" + sum;
        } else {
            sumText = sum + "";
        }
        tv.setText(sumText);
    }

    /**
     * Adds 1 to the displayed year and sets it as the year title.
     * @param v     The previous year button that was pressed
     */
    public void nextYearPressed(View v) {
        this.year += 1;
        String nextYear = this.year + "";
        yearSwitcher.setText(nextYear);
    }

    /**
     * Subtracts 1 from the displayed year and sets it as the year title.
     * @param v     The previous year button that was pressed
     */
    public void previousYearPressed(View v) {
        this.year -= 1;
        String previousYear = this.year + "";
        yearSwitcher.setText(previousYear);
    }

    /**
     * Adds a text change listener for a text view which is used to
     * update the yearly view.
     * @param tv    The text view
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
                iterateSums();
                run();
            }
        });
    }

    /**
     * For preparing the options menu.
     *
     * @param menu  The menu that should be prepared
     * @return      True if the menu is to be shown
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * For initialization of the contents of the options menu.
     *
     * @param menu  The options menu where a custom menu is put in
     * @return      True to be able to show the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.yearly_menu, menu);

        this.menu = menu;

        return true;
    }

    /**
     *
     * @param item  The options menu item that was selected
     * @return      True if selection matches one of the defined options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        MenuItem removeCurrentYearsData = this.menu.findItem(R.id.removeCurrentYearsData);
        MenuItem removeAllYearsData = this.menu.findItem(R.id.removeAllYearsData);

        if (item == removeCurrentYearsData) {
            showYearlyDataRemovalDialog();
            return true;
        } else if (item == removeAllYearsData) {
            showAllYearsDataRemovalDialog();
            return true;
        }
        return false;
    }

    /**
     * Show the yearly data removal dialog for the user to decide whether to
     * really remove the data or cancel.
     */
    public void showYearlyDataRemovalDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,
                R.style.AlertDialogCustomized);
        alertDialog.setTitle("Poista kaikki vuoden tiedot?");

        alertDialog.setPositiveButton("Poista", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removeCurrentYearsData();
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
     * Show the all years' data removal dialog for the user to decide whether to
     * really remove the data or cancel.
     */
    public void showAllYearsDataRemovalDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,
                R.style.AlertDialogCustomized);
        alertDialog.setTitle("Poista kaikki vuoden tiedot?");

        alertDialog.setPositiveButton("Poista", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removeAllYearsData();
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
     * Removes all entries from current year and informs the user about this.
     */
    public void removeCurrentYearsData() {
        long id = -1L;

        Cursor c = dbManager.getCursorForYear(year);
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
            Toast.makeText(getApplicationContext(),"Vuoden tiedot poistettu",
                    Toast.LENGTH_LONG).show();
            iterateSums();
            run();
        }
    }

    /**
     * Removes all entries from all years
     * and informs the user about this.
     */
    public void removeAllYearsData() {
        dbManager.deleteAll();

        Toast.makeText(getApplicationContext(),"Kaikki tiedot poistettu",
                Toast.LENGTH_LONG).show();
        iterateSums();
        run();
    }
}
