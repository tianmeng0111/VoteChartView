package com.tm.chart.vote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List<VoteChartView.Data> list = MainActivity.initData();
        ListView lv = findViewById(R.id.lv);

        MyAdapter adapter = new MyAdapter(ListActivity.this, list);
        lv.setAdapter(adapter);
    }

    private static class MyAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;
        private  List<VoteChartView.Data> listBar;

        public MyAdapter(Context context,  List<VoteChartView.Data> listBar) {
            this.context = context;
            list = new ArrayList<>();
            list.add("djfidfj");
            list.add("iieieooe");
            list.add("11111");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            list.add("455767fgdfg");
            this.listBar = listBar;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item, null);
            }
            TextView tvTitle = convertView.findViewById(R.id.tv_title);
            VoteChartView barChartView = convertView.findViewById(R.id.barchartview);

            tvTitle.setText( list.get(position));
            if (position == 1) {
                barChartView.setVisibility(View.VISIBLE);
                barChartView.setData(listBar);

            } else {
                barChartView.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}
