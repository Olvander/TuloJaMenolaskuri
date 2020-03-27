package fi.tuni.tiko.tulojamenolaskuri;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthlyViewActivity extends AppCompatActivity {

    private RecyclerView monthlyRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        monthlyRecyclerView = findViewById(R.id.monthlyRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        monthlyRecyclerView.setLayoutManager(layoutManager);

        ArrayList<String> list = new ArrayList<String>();
        list.add("Hello");
        list.add("World");
        myAdapter = new MyAdapter(list);
        monthlyRecyclerView.setAdapter(myAdapter);
    }
}
