package fi.tuni.tiko.tulojamenolaskuri;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MonthlyViewViewHolder> {

    private ArrayList<String> model;

    public MyAdapter(ArrayList<String> model) {
        this.model = model;
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public MonthlyViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    }

    @Override
    public void onBindViewHolder(MonthlyViewViewHolder holder, int position) {

    }

    public void add(String listItem) {
        model.add(listItem);
        int position = model.size() - 1;
        notifyItemInserted(position);
    }
    public void remove(String listItem) {
        int position = model.indexOf(listItem);
        model.remove(position);
        notifyItemRemoved(position);
    }
}
