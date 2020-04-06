package fi.tuni.tiko.tulojamenolaskuri;

import java.util.Calendar;
import java.util.Locale;

/**
 * This class contains the entry data.
 * The month of the date is 1 less than the real month by default.
 */
public class EntryData {
    /**
     * The date specified for the entry data
     */
    private Calendar date;

    /**
     * The description of the entry data
     */
    private String description;

    /**
     * The sum of the entry data
     */
    private String sum;

    /**
     * Constructor that iitializes some fields
     * @param date          The date
     * @param description   The description
     * @param sum           The sum
     */
    public EntryData(Calendar date, String description, String sum) {
        this.date = date;
        this.description = description;
        this.sum = sum;
    }

    /**
     * Gets the date as a Calendar instance
     * @return  The date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Gets the day
     * @return  The day
     */
    public int getDay() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gets the week day
     * @return  The week day
     */
    public String getWeekDay() {
        return date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault());
    }

    /**
     * Gets the month
     * @return  The month
     */
    public int getMonth() {
        return date.get(Calendar.MONTH);
    }

    /**
     * Gets the year
     * @return  The year
     */
    public int getYear() {
        return date.get(Calendar.YEAR);
    }

    /**
     * Gets the date as a string
     * @return  The date
     */
    public String getDateToString() {
        int month = getMonth() + 1;
        String date = getWeekDay() + " " + getDay() + "." + month + "." +
                getYear();
        return date;
    }

    /**
     * Sets the date for the entry data
     * @param date  The date
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * Gets the description of the entry data
     * @return  The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the entry data
     * @param description   The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the sum
     * @return  The sum of the entry data
     */
    public String getSum() {
        return sum;
    }

    /**
     * Sets the sum.
     * @param sum   The sum of the entry data
     */
    public void setSum(String sum) {
        this.sum = sum;
    }
}
