package fi.tuni.tiko.tulojamenolaskuri;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MonthlyViewViewHolder extends RecyclerView.ViewHolder {

    public TextView textView1;
    public TextView textView2;
    public TextView textView3;

    public MonthlyViewViewHolder(View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.textView1);
        textView2 = itemView.findViewById(R.id.textView2);
        textView3 = itemView.findViewById(R.id.textView3);
    }
}
