package fi.tuni.tiko.tulojamenolaskuri;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom view holder for the monthly entries view.
 */
public class MonthlyViewViewHolder extends RecyclerView.ViewHolder {

    /**
     * TextView for the date defined in the entry
     */
    public TextView textView1;

    /**
     * TextView for the description of the entry.
     */
    public TextView textView2;

    /**
     * TextView for the sum of the entry.
     */
    public TextView textView3;

    /**
     * Constructor for the montly view viewholder
     * @param itemView  The view for the RecyclerView item.
     */
    public MonthlyViewViewHolder(View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.textView1);
        textView2 = itemView.findViewById(R.id.textView2);
        textView3 = itemView.findViewById(R.id.textView3);
    }
}
