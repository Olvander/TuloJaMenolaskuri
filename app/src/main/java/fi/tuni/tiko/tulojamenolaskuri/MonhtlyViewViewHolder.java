package fi.tuni.tiko.tulojamenolaskuri;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MonhtlyViewViewHolder extends RecyclerView.ViewHolder {

    private TextView textView1;
    private TextView textView2;

    public MonhtlyViewViewHolder(View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.textView1);
        textView2 = itemView.findViewById(R.id.textView2);
    }
}
