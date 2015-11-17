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
import com.seven.emsmeals.ScheduleDataParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

@SuppressLint("ValidFragment")
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
        int[] days = DateCalculator();

        SharedPreferences data = SchActivity.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        String schdb = data.getString("sch", "");
        TextView monday = (TextView) view.findViewById(R.id.mondaysch);
        TextView tuesday = (TextView) view.findViewById(R.id.tuesdaysch);
        TextView wednesday = (TextView) view.findViewById(R.id.wednesdaysch);
        TextView thursday = (TextView) view.findViewById(R.id.thursdaysch);
        TextView friday = (TextView) view.findViewById(R.id.fridaysch);

        if(schdb != "") {
            Document sch = Jsoup.parse(schdb);
            ScheduleData[] schs = ScheduleDataParser.parse(sch);

            try {
                String mondaysch = schs[days[1] - 1].schedule;
                monday.setText(mondaysch);
            } catch (NullPointerException e) {
                monday.setText(NoSch);
            }

            try {
                String tuesdaysch = schs[days[2] - 1].schedule;
                tuesday.setText(tuesdaysch);
            } catch (NullPointerException e) {
                tuesday.setText(NoSch);
            }

            try {
                String wednesdaysch = schs[days[3] - 1].schedule;
                wednesday.setText(wednesdaysch);
            } catch (NullPointerException e) {
                wednesday.setText(NoSch);
            }

            try {
                String thursdaysch = schs[days[4] - 1].schedule;
                thursday.setText(thursdaysch);
            } catch (NullPointerException e) {
                thursday.setText(NoSch);
            }

            try {
                String fridaysch = schs[days[5] - 1].schedule;
                friday.setText(fridaysch);
            } catch (NullPointerException e) {
                friday.setText(NoSch);
            }
        }
    }

    public static int[] DateCalculator()
    {
        // 0은 일요일 날짜, 1은 월요일 날짜 ...

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int dw = cal.get(Calendar.DAY_OF_WEEK);
        int[] days = new int[7];

        for (int i=0; i<7; i++) {
            days[i] = day-(dw-1)+i;
        }
        return days;
    }
}
