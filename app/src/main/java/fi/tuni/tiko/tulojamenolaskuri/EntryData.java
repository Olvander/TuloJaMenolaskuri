package fi.tuni.tiko.tulojamenolaskuri;

import java.util.Calendar;
import java.util.Locale;

public class EntryData {
    private Calendar date;
    private String description;
    private String sum;

    public EntryData(Calendar date, String description, String sum) {
        this.date = date;
        this.description = description;
        this.sum = sum;
    }

    public Calendar getDate() {
        return date;
    }

    public int getDay() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public String getWeekDay() {
        return date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault());
    }

    public int getMonth() {
        return date.get(Calendar.MONTH);
    }

    public int getYear() {
        return date.get(Calendar.YEAR);
    }

    public String getDateToString() {
        String date = getWeekDay() + " " + getDay() + "." + getMonth() + "." +
                getYear();
        System.out.println(date);
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
