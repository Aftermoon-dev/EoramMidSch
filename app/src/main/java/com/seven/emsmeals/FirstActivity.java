package com.seven.emsmeals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class FirstActivity extends Fragment {
    public static Context mContext;
    String TodayTable;

    public FirstActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_first, null);

        TimeTable();
        TextView TimeTable = (TextView) view.findViewById(R.id.timetabletext);
        TimeTable.setText(TodayTable);

        MealLoad(view);
        SchLoad(view);
        return view;
    }

    public void SchLoad(View view) {
        String NoSch = getResources().getString(R.string.nosch);
        Calendar cal = Calendar.getInstance();
        int days = cal.get(Calendar.DAY_OF_MONTH);

        SharedPreferences data = FirstActivity.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        String schdb = data.getString("sch", "");
        TextView today = (TextView) view.findViewById(R.id.firstsch);

        if (schdb != "") {
            Document sch = Jsoup.parse(schdb);
            try {
                ScheduleData[] schs = ScheduleDataParser.parse(sch);
                String todaydata = schs[days - 1].schedule;
                today.setText(todaydata);
            } catch (Exception e) {
                today.setText(NoSch);
            }
        } else {
            today.setText(NoSch);
        }
    }

    public void MealLoad(View view) {
        String NoMeal = getResources().getString(R.string.nomeal);
        Calendar cal = Calendar.getInstance();
        int days = cal.get(cal.DAY_OF_MONTH);

        SharedPreferences data = FirstActivity.this.getActivity().getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdb = data.getString("meal", "");
        TextView today = (TextView) view.findViewById(R.id.firstmeal);

        if (mealdb != "") {
            Document meal = Jsoup.parse(mealdb);
            try {
                MenuData[] meals = MenuDataParser.parse(meal);
                String todaydata = meals[days - 1].lunch;
                today.setText(todaydata);
            } catch (Exception e) {
                today.setText(NoMeal);
            }
        } else {
            today.setText(NoMeal);
        }
    }

    public void TimeTable() {
        Calendar cal = Calendar.getInstance();
        int dw = cal.get(Calendar.DAY_OF_WEEK);

        if (dw == 1 || dw == 7) {
            TodayTable = getString(R.string.Weekend);
        }
        if (dw == 2) {
            SharedPreferences montable = FirstActivity.this.getActivity().getSharedPreferences("montable", Context.MODE_MULTI_PROCESS);
            TodayTable = montable.getString("montable", getString(R.string.notime));
        }

        if (dw == 3) {
            SharedPreferences tuetable = FirstActivity.this.getActivity().getSharedPreferences("tuetable", Context.MODE_MULTI_PROCESS);
            TodayTable = tuetable.getString("tuetable", getString(R.string.notime));
        }

        if (dw == 4) {
            SharedPreferences wedtable = FirstActivity.this.getActivity().getSharedPreferences("wedtable", Context.MODE_MULTI_PROCESS);
            TodayTable = wedtable.getString("wedtable", getString(R.string.notime));
        }

        if (dw == 5) {
            SharedPreferences thutable = FirstActivity.this.getActivity().getSharedPreferences("thutable", Context.MODE_MULTI_PROCESS);
            TodayTable = thutable.getString("thutable", getString(R.string.notime));
        }

        if (dw == 6) {
            SharedPreferences fritable = FirstActivity.this.getActivity().getSharedPreferences("fritable", Context.MODE_MULTI_PROCESS);
            TodayTable = fritable.getString("fritable", getString(R.string.notime));
        }
    }
}
