package com.tm.chart.vote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<VoteChartView.Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VoteChartView barChartView = findViewById(R.id.barchartview);
        list = initData();
        barChartView.setData(list);

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChartView.reset();
            }
        });
    }

    public static List<VoteChartView.Data> initData() {
        List<VoteChartView.Data> list = new ArrayList<>();
        VoteChartView.Data data = new VoteChartView.Data();
        data.setPercent(0.5f);
        data.setText("支持甲方");
        list.add(data);

        data = new VoteChartView.Data();
        data.setPercent(0.3f);
        data.setText("支持乙方");
        list.add(data);
        data = new VoteChartView.Data();
        data.setPercent(0.9f);
        data.setText("支持其他");
        list.add(data);
        data = new VoteChartView.Data();
        data.setPercent(0.6f);
        data.setText("不关心");
        list.add(data);
        data = new VoteChartView.Data();
        data.setPercent(0.2f);
        data.setText("一二三一二三一二三一二三一二三");
        list.add(data);
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_to_second:
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
