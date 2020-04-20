package fi.tuni.tiko.tulojamenolaskuri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The main activity that holds three buttons that can currently be used to
 * access the Add new entry activity and monthly view activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * For initialization of the view.
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Proceeds to AddNewActivity when a button is pressed.
     * @param v The Add New Income or Expense button
     */
    public void onAddNewPressed(View v) {
        Intent i = new Intent(this, AddNewActivity.class);
        startActivity(i);
    }

    /**
     * Proceeds to MonthlyViewActivity when a button is pressed.
     * @param v The monthly view button
     */
    public void onMonthlyViewPressed(View v) {
        Intent i = new Intent(this, MonthlyViewActivity.class);
        startActivity(i);
    }

    /**
     * Proceeds to YearlyViewActivity when a button is pressed.
     * @param v The yearly view button
     */
    public void onYearlyViewPressed(View v) {
        Intent i = new Intent(this, YearlyViewActivity.class);
        startActivity(i);
    }


    /**
     * Force exit app on back pressed instead of going back
     * to previous activity
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
