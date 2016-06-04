package com.seven.emsmeals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

        // 급식 에러 스트링
        String NoMeal = getResources().getString(R.string.nomeal);

        // 날짜 계산
        String[] monthday = DateCalculator();

        // 이번 달 급식 메뉴 SP 로드
        SharedPreferences data = MealActivity.this.getActivity().getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdb = data.getString("meal", "");

        // 다음 달 급식 메뉴 SP 로드
        SharedPreferences data1 = MealActivity.this.getActivity().getSharedPreferences("mealnm", Context.MODE_MULTI_PROCESS);
        String mealdb1 = data1.getString("mealnm", "");

        // 이번 달 (데이터를 다운한 달) SP 로드
        SharedPreferences data2 = MealActivity.this.getActivity().getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
        int months = data2.getInt("month", 0);

        // 텍스트뷰 찾기
        TextView monday = (TextView) view.findViewById(R.id.mondaymeal);
        TextView tuesday = (TextView) view.findViewById(R.id.tuesdaymeal);
        TextView wednesday = (TextView) view.findViewById(R.id.wednesdaymeal);
        TextView thursday = (TextView) view.findViewById(R.id.thursdaymeal);
        TextView friday = (TextView) view.findViewById(R.id.fridaymeal);

        // 메뉴 SP가 비어있지 않은경우
        if (!mealdb.equals("")) {

            MenuData[] meals = new MenuData[31];
            MenuData[] nmmeals = new MenuData[31];
            try {
                // 현재 달의 급식 데이터 파싱
                Document meal = Jsoup.parse(mealdb);
                meals = MenuDataParser.parse(meal);

                // 다음 달의 급식 데이터 파싱
                Document mealnm = Jsoup.parse(mealdb1);
                nmmeals = MenuDataParser.parse(mealnm);
            } catch (Exception e) {
                Log.d("SchActivity", "Parsing Error!");
            }

            // 월요일
            try {
                // 날짜 계산에서 가져온 MMdd 형식의 날짜 스트링을 자름
                int Mondaym = Integer.parseInt(monthday[1].substring(0, 2));
                int Mondayd = Integer.parseInt(monthday[1].substring(2, 4));

                // 저장된 달과 월요일의 달이 같을경우
                if(Mondaym == months)
                {
                    // 이번달 급식 파싱
                    monday.setText(meals[Mondayd - 1].lunch);
                }
                else // 아니라면
                {
                    // 다음달 급식 파싱
                    monday.setText(nmmeals[Mondayd - 1].lunch);
                }

            } catch (Exception e) {
                // 에러 텍스트 설정
                monday.setText(NoMeal);
            }

            // 화요일
            try {
                int Tuesdaym = Integer.parseInt(monthday[2].substring(0, 2));
                int Tuesdayd = Integer.parseInt(monthday[2].substring(2, 4));

                if(Tuesdaym == months)
                {
                    tuesday.setText(meals[Tuesdayd - 1].lunch);
                }
                else
                {
                    tuesday.setText(nmmeals[Tuesdayd - 1].lunch);
                }

            } catch (Exception e) {
                tuesday.setText(NoMeal);
            }

            // 수요일
            try {
                int Wednesdaym = Integer.parseInt(monthday[3].substring(0, 2));
                int Wednesdayd = Integer.parseInt(monthday[3].substring(2, 4));

                if(Wednesdaym == months)
                {
                    wednesday.setText(meals[Wednesdayd - 1].lunch);
                }
                else
                {
                    wednesday.setText(nmmeals[Wednesdayd - 1].lunch);
                }

            } catch (Exception e) {
                wednesday.setText(NoMeal);
            }

            try {
                int Thursdaym = Integer.parseInt(monthday[4].substring(0, 2));
                int Thursdayd = Integer.parseInt(monthday[4].substring(2, 4));

                if(Thursdaym == months)
                {
                    thursday.setText(meals[Thursdayd - 1].lunch);
                }
                else
                {
                    thursday.setText(nmmeals[Thursdayd - 1].lunch);
                }

            } catch (Exception e) {
                thursday.setText(NoMeal);
            }

            try {
                int Fridaym = Integer.parseInt(monthday[5].substring(0, 2));
                int Fridayd = Integer.parseInt(monthday[5].substring(2, 4));

                if(Fridaym == months)
                {
                    friday.setText(meals[Fridayd - 1].lunch);
                }
                else
                {
                    friday.setText(nmmeals[Fridayd - 1].lunch);
                }

            } catch (Exception e) {
                friday.setText(NoMeal);
            }
        }
    }

    public static String[] DateCalculator()
    {
        Calendar cal = Calendar.getInstance();
        List<String> DayList = new ArrayList<String>();
        DayList.add(new SimpleDateFormat("MMdd").format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        for(int i=1; i < 7; i++) {
            cal.add(Calendar.DATE, 1);
            DayList.add(new SimpleDateFormat("MMdd").format(cal
                    .getTime()));
        }

        String[] DayListString = DayList.toArray(new String[DayList.size()]);

        return DayListString;
    }

}

