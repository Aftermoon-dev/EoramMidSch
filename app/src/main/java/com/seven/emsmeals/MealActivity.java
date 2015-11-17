package com.seven.emsmeals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class MealActivity extends Fragment {
    public static Context Contexts;

    public MealActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal, null);
        MealLoad(Contexts, view);
        return view;
    }

    public void MealLoad(Context context, View view) {
        String NoMeal = getResources().getString(R.string.nomeal);
        int[] days = DateCalculator();

        SharedPreferences data = MealActivity.this.getActivity().getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdb = data.getString("meal", "");

        TextView monday = (TextView) view.findViewById(R.id.mondaymeal);
        TextView tuesday = (TextView) view.findViewById(R.id.tuesdaymeal);
        TextView wednesday = (TextView) view.findViewById(R.id.wednesdaymeal);
        TextView thursday = (TextView) view.findViewById(R.id.thursdaymeal);
        TextView friday = (TextView) view.findViewById(R.id.fridaymeal);

        if (mealdb != "") {
            Document meal = Jsoup.parse(mealdb);
            MenuData[] meals = MenuDataParser.parse(meal);

            try {
                String mondaymeal = meals[days[1] - 1].lunch;
                monday.setText(mondaymeal);
            } catch (Exception e) {
                monday.setText(NoMeal);
            }

            try {
                String tuesdaymeal = meals[days[2] - 1].lunch;
                tuesday.setText(tuesdaymeal);
            } catch (Exception e) {
                tuesday.setText(NoMeal);
            }

            try {
                String wednesdaymeal = meals[days[3] - 1].lunch;
                wednesday.setText(wednesdaymeal);
            } catch (NullPointerException e) {
                wednesday.setText(NoMeal);
            }

            try {
                String thursdaymeal = meals[days[4] - 1].lunch;
                thursday.setText(thursdaymeal);
            } catch (Exception e) {
                thursday.setText(NoMeal);
            }

            try {
                String fridaymeal = meals[days[5] - 1].lunch;
                friday.setText(fridaymeal);
            } catch (Exception e) {
                friday.setText(NoMeal);
            }
        }
    }

    public static int[] DateCalculator()
    {
        // 0은 일요일 날짜, 1은 월요일 날짜 ..

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int dw = cal.get(Calendar.DAY_OF_WEEK);
        int daymax = cal.getActualMaximum(Calendar.DATE);
        int[] days = new int[7];

        for (int i=0; i<7; i++) {
            days[i] = day-(dw-1)+i;
        }

        return days;
    }
}

