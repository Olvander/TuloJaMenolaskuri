package fi.tuni.tiko.tulojamenolaskuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class manages the entry data database.
 * The month in this database is saved in the such a format
 * that January is 1 instead of 0.
 *
 * @author Olli Pertovaara
 * @version 1.7
 * @since   2020.04.20
 */
public class DatabaseManager extends SQLiteOpenHelper {

    /**
     * The context which this instance was called from
     */
    private Context context;

    /**
     * The final String value for the database name
     */
    private static final String DATABASE_NAME = "EntriesDatabase.db";

    /**
     * The final int value for the database version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The final String value for the table name for the entry data
     */
    private static final String TABLE_NAME = "entry_data";

    /**
     * The final String value for the database column "id"
     */
    private static final String COLUMN_ID = "id";

    /**
     * The final String value for the database column "description"
     */
    private static final String COLUMN_DESCRIPTION = "description";

    /**
     * The final String value for the database column "sum"
     */
    private static final String COLUMN_SUM = "sum";

    /**
     * The final String value for the database column "year"
     */
    private static final String COLUMN_YEAR = "year";

    /**
     * The final String value for the database column "month"
     */
    private static final String COLUMN_MONTH = "month";

    /**
     * The final String value for the database column "day"
     */
    private static final String COLUMN_DAY = "day";

    /**
     * Contains all the entry data after getAllByMonthAndYear() is called.
     */
    private ArrayList<EntryData> entryData;

    /**
     * The SQLiteDatabase object used for interacting with the database.
     */
    private SQLiteDatabase db;

    /**
     * Constructor for this class. For initialization.
     * @param context   The context of the calling instance.
     */
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        openDatabase();
        entryData = new ArrayList<EntryData>();
    }

    /**
     * A method that is called on creation of the database.
     * Sets the parameter as the SQLiteDatabase field.
     * @param db    The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * An inherited method from the SQLiteOpenHelper class. Not used.
     * @param db            The database
     * @param oldVersion    The old version of the database
     * @param newVersion    The new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Open the database connection or create a database if it does not exist.
     */
    public void openDatabase() {
        try {
            db = this.context.openOrCreateDatabase(DATABASE_NAME,
                    Context.MODE_PRIVATE, null);

            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_DESCRIPTION + " TEXT, " + COLUMN_SUM +
                    " TEXT," + COLUMN_YEAR + " INTEGER, " +
                    COLUMN_MONTH + " INTEGER, " + COLUMN_DAY + " INTEGER);";

            db.execSQL(sql);

        } catch(SQLiteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find an entry data item in the database based on its id.
     * @param ID    The id of the database item
     * @return      An entry data item
     */
    public EntryData findItemById(Long ID) {
        EntryData entryData = null;

        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + ID +
                    " LIMIT 1;";

            Cursor c = db.rawQuery(sql, null);

            c.moveToFirst();

            String description = c.getString(1);
            String sum = c.getString(2);
            int year = c.getInt(3);
            int month = c.getInt(4);
            int day = c.getInt(5);
            Calendar date = Calendar.getInstance();
            date.set(year, month - 1, day);

            c.close();

            entryData = new EntryData(date, description, sum);

        } catch(SQLiteException e) {
            e.printStackTrace();
        }
        return entryData;
    }

    /**
     * Get all entry data items as ArrayList searched by month and year.
     * @param yearToDisplay     The year parameter to search for
     * @param monthToDisplay    The month to search for
     * @return                  The entry data ArrayList
     */
    public ArrayList<EntryData> getAllByMonthAndYear(int yearToDisplay,
                                                     int monthToDisplay) {
        try {

            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE year = " + yearToDisplay + " AND month = " +
                    monthToDisplay + " ORDER BY day ASC;", null);

            this.entryData.clear();

            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {

                Calendar date = Calendar.getInstance();

                int year = c.getInt(3);
                int month = c.getInt(4);
                int day = c.getInt(5);

                date.set(year, month - 1, day);
                entryData.add(new EntryData(date, c.getString(1),
                        c.getString(2)));

                c.moveToNext();
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.entryData;
    }

    /**
     * The total sum of the the monthly items to be displayed.
     * @param year  The year to filter the results with
     * @param month The month to filter the results with
     * @return      The sum of the monthly items
     */
    public double getSumOfMonthlyItems(int year, int month) {

        double sum = 0;

        try {

            Cursor c = db.rawQuery("SELECT SUM(sum) AS MONTHLY_TOTAL FROM " +
                    TABLE_NAME + " WHERE year = " + year + " AND month = " +
                    month + ";", null);

            c.moveToFirst();

            sum = c.getDouble(0);

            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sum;
    }

    /**
     * Gets the Cursor object for a specific month's and year's items in the
     * database.
     * @param year  The year to search for
     * @param month The month to search for
     * @return      The cursor for a specific month's and year's items
     */
    public Cursor getCursorForMonthAndYear(int year, int month) {
        Cursor c = null;

        try {

            c = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE (year = " + year + " AND month = " +
                    month + ") ORDER BY day ASC;", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Gets the Cursor object for a specific year's items in the database.
     * @param year  The year to search for
     * @return      The cursor for a specific year's items
     */
    public Cursor getCursorForYear(int year) {
        Cursor c = null;

        try {

            c = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE (year = " + year + ");", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * A method for deleting one database row based on an id.
     * @param ID    The id of the row to delete
     */
    public void deleteEntry(Long ID) {
        try {
            String sql = "DELETE FROM " + TABLE_NAME +
                    " WHERE id = " + ID + ";";
            db.execSQL(sql);

        } catch(SQLiteException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for saving new entry data to the database.
     * @param entryData The entry data to be saved
     */
    public void saveEntry(EntryData entryData) {
        int month = entryData.getMonth() + 1;
        String sql = "INSERT INTO " + TABLE_NAME +
                " (description, sum, year, month, day) VALUES ('" +
                entryData.getDescription() + "', '" + entryData.getSum() +
                "', " + entryData.getYear() + ", " + month +
                ", " + entryData.getDay() + ");";

        db.execSQL(sql);
    }

    /**
     * A method to delete all entries and drop the table.
     */
    public void deleteAll() {
        String sql = "DROP TABLE " + TABLE_NAME + ";";
        db.execSQL(sql);
    }

    /**
     * A method for updating an existing entry with new entry data.
     * @param entryData The new entry data
     * @param ID        The id of the database row to update
     */
    public void updateEntry(EntryData entryData, Long ID) {
        ContentValues values = new ContentValues();
        values.put("description", entryData.getDescription());
        values.put("sum", entryData.getSum());
        values.put("year", entryData.getYear());
        values.put("month", entryData.getMonth() + 1);
        values.put("day", entryData.getDay());

        db.update(TABLE_NAME, values, "id = ?", new String[]{ID + ""});
    }
}
