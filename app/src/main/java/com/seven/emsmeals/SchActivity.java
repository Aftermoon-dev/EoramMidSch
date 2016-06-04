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

import com.seven.emsmeals.ScheduleDataParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SchActivity extends Fragment {
    public static Context mContext;

    public SchActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sch, null);
        SchLoad(mContext, view);
        return view;
    }

    public void SchLoad(Context context, View view)
    {
        String NoSch = getResources().getString(R.string.nosch);
        // 날짜 계산
        String[] monthday = DateCalculator();

        String schdb = new String();
        String schdb1 = new String();
        int months;

        SharedPreferences data = SchActivity.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        schdb = data.getString("sch", "");

        // 다음 달 급식 메뉴 SP 로드
        SharedPreferences data1 = SchActivity.this.getActivity().getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
        schdb1 = data1.getString("schnm", "");

        // 이번 달 (데이터를 다운한 달) SP 로드
        SharedPreferences data2 = SchActivity.this.getActivity().getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
        months = data2.getInt("month", 0);

        TextView monday = (TextView) view.findViewById(R.id.mondaysch);
        TextView tuesday = (TextView) view.findViewById(R.id.tuesdaysch);
        TextView wednesday = (TextView) view.findViewById(R.id.wednesdaysch);
        TextView thursday = (TextView) view.findViewById(R.id.thursdaysch);
        TextView friday = (TextView) view.findViewById(R.id.fridaysch);

        if (!schdb.equals("")) {

            ScheduleData[] schs = new ScheduleData[31];
            ScheduleData[] schnms = new ScheduleData[31];

            try {
                Document sch = Jsoup.parse(schdb);
                schs = ScheduleDataParser.parse(sch);

                Document schnm = Jsoup.parse(schdb1);
                schnms = ScheduleDataParser.parse(schnm);
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
                    monday.setText(schs[Mondayd - 1].schedule);
                }
                else // 아니라면
                {
                    // 다음달 급식 파싱
                    monday.setText(schnms[Mondayd - 1].schedule);
                }

            } catch (Exception e) {
                // 에러 텍스트 설정
                monday.setText(NoSch);
            }

            // 화요일
            try {
                int Tuesdaym = Integer.parseInt(monthday[2].substring(0, 2));
                int Tuesdayd = Integer.parseInt(monthday[2].substring(2, 4));

                if(Tuesdaym == months)
                {
                    tuesday.setText(schs[Tuesdayd - 1].schedule);
                }
                else
                {
                    tuesday.setText(schnms[Tuesdayd - 1].schedule);
                }

            } catch (Exception e) {
                tuesday.setText(NoSch);
            }

            // 수요일
            try {
                int Wednesdaym = Integer.parseInt(monthday[3].substring(0, 2));
                int Wednesdayd = Integer.parseInt(monthday[3].substring(2, 4));

                if(Wednesdaym == months)
                {
                    wednesday.setText(schs[Wednesdayd - 1].schedule);
                }
                else
                {
                    wednesday.setText(schnms[Wednesdayd - 1].schedule);
                }

            } catch (Exception e) {
                wednesday.setText(NoSch);
            }

            try {
                int Thursdaym = Integer.parseInt(monthday[4].substring(0, 2));
                int Thursdayd = Integer.parseInt(monthday[4].substring(2, 4));

                if(Thursdaym == months)
                {
                    thursday.setText(schs[Thursdayd - 1].schedule);
                }
                else
                {
                    thursday.setText(schnms[Thursdayd - 1].schedule);
                }

            } catch (Exception e) {
                thursday.setText(NoSch);
            }

            try {
                int Fridaym = Integer.parseInt(monthday[5].substring(0, 2));
                int Fridayd = Integer.parseInt(monthday[5].substring(2, 4));

                if(Fridaym == months)
                {
                    friday.setText(schs[Fridayd - 1].schedule);
                }
                else
                {
                    friday.setText(schnms[Fridayd - 1].schedule);
                }

            } catch (Exception e) {
                friday.setText(NoSch);
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
