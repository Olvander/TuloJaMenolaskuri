package fi.tuni.tiko.tulojamenolaskuri;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Custom adapter needed for the RecyclerView of the Monthly View.
 *
 * @author Olli Pertovaara
 * @version 1.7
 * @since   2020.04.20
 */
public class MyAdapter extends RecyclerView.Adapter<MonthlyViewViewHolder> {

    private ArrayList<EntryData> model;
    private DatabaseManager dbManager;
    private int year;
    private int month;
    private Context context;

    /**
     * Constructor for the MyAdapter class. Used for field initialization.
     * @param context   The calling class instance
     * @param model     The list of entry data
     * @param year      The current year to display
     * @param month     The current month
     */
    public MyAdapter(Context context, ArrayList<EntryData> model, int year,
                     int month) {
        this.model = model;
        this.year = year;
        this.month = month;
        dbManager = new DatabaseManager(context);
    }

    /**
     * Method that helps in finding the corresponding row from the database
     * based on id, year and month.
     * @param position  The current position of the model arraylist
     * @return          The id from the database
     */
    @Override
    public long getItemId(int position) {
        long id = -1L;

        Cursor c = dbManager.getCursorForMonthAndYear(year, month);
        if (c != null) {
            c.moveToPosition(position);
            id = c.getLong(0);
        }
        return id;
    }

    /**
     * Getter for the item count
     * @return  The size of the recyclerview model
     */
    @Override
    public int getItemCount() {
        return model.size();
    }

    /**
     * A method called by the layout manager.
     * @param parent    The parent ViewGroup that a new view will be added into
     * @param viewType  The view type of the new view
     * @return          The created view holder
     */
    @Override
    @NonNull
    public MonthlyViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.monthly_item, parent, false);
        MonthlyViewViewHolder viewHolder = new MonthlyViewViewHolder(view);
        return viewHolder;
    }

    /**
     * Called by the RecyclerView for displaying data at a certain position.
     * @param holder    The viewholder that is updated
     * @param position  The position of the item in the data set model
     */
    @Override
    public void onBindViewHolder(MonthlyViewViewHolder holder, int position) {
        String date = model.get(position).getDateToString();
        String description = model.get(position).getDescription();
        String sum = model.get(position).getSum() + " €";
        holder.textView1.setText(date);
        holder.textView2.setText(description);
        holder.textView3.setText(sum);

        if (model.get(position).getSum().startsWith("+")) {
            holder.textView3.setTextColor(Color.parseColor("#99cc00"));
        } else if (model.get(position).getSum().startsWith("-")) {
            holder.textView3.setTextColor(Color.parseColor("#ce2222"));
        }
    }

    public void add(EntryData entryData) {
        model.add(entryData);
        int position = model.size() - 1;
        notifyItemInserted(position);
    }
    public void remove(EntryData entryData) {
        int position = model.indexOf(entryData);
        model.remove(position);
        notifyItemRemoved(position);
    }
}
