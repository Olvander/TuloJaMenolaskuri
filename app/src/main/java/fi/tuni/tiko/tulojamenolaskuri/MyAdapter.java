package fi.tuni.tiko.tulojamenolaskuri;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MonthlyViewViewHolder> {

    private ArrayList<EntryData> model;

    public MyAdapter(ArrayList<EntryData> model) {
        this.model = model;
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    @NonNull
    public MonthlyViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.monthly_item, parent, false);
        MonthlyViewViewHolder viewHolder = new MonthlyViewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MonthlyViewViewHolder holder, int position) {
        String date = model.get(position).getDateToString();
        String description = model.get(position).getDescription();
        String sum = model.get(position).getSum();
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
