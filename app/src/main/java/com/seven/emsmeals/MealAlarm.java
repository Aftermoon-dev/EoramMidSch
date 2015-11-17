package com.seven.emsmeals;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Calendar;

/**
 * Created by 민재 on 2015-04-08.
 */
public class MealAlarm extends BroadcastReceiver {
    long[] vibrate = { 100, 1000 }; // 진동
    String todaydata;

    @Override
    public void onReceive(Context context, Intent arg1) {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(arg1.getAction().equals("com.seven.emsmeals.mealalarm")) {
            Calendar cal = Calendar.getInstance();
            int days = cal.get(cal.DAY_OF_MONTH);
            int month = cal.get(cal.MONTH) + 1;
            int hour = cal.get(cal.HOUR_OF_DAY);
            int minute = cal.get(cal.MINUTE);

            SharedPreferences data = context.getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
            int dmonth = data.getInt("month", 0);

            SharedPreferences mealhour = context.getSharedPreferences("mealhour", Context.MODE_MULTI_PROCESS);
            int MealHour = mealhour.getInt("mealhour", 21);

            SharedPreferences mealminute = context.getSharedPreferences("mealminute", Context.MODE_MULTI_PROCESS);
            int MealMinute = mealminute.getInt("mealminute", 0);

            if (dmonth == month)
            {
                SharedPreferences mealdata = context.getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
                String mealdb = mealdata.getString("meal", "");

                SharedPreferences alarmdayset = context.getSharedPreferences("alarmdayset", Context.MODE_MULTI_PROCESS);
                int AlarmDaySet = alarmdayset.getInt("alarmdayset", 1);

                Document meal = Jsoup.parse(mealdb);
                MenuData[] meals = MenuDataParser.parse(meal);

                if(hour == MealHour && minute == MealMinute )
                {
                    if(AlarmDaySet == 0)
                    {
                        String todaydata = meals[days - 1].lunch;
                        if (todaydata != "급식이 없습니다.") {
                            Log.d("Alarm", "급식 알람 작동");
                            Notification.BigTextStyle meala = new Notification.BigTextStyle(
                                    new Notification.Builder(context)
                                            .setContentTitle("어람중학교 오늘의 급식")
                                            .setContentText(todaydata)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setAutoCancel(true)
                                            .setVibrate(vibrate)
                                            .setTicker("어람중 오늘의 급식은?"))
                                    .setSummaryText("어람중학교")
                                    .bigText(todaydata);
                            manager.notify(1, meala.build());
                        }
                    }
                    else if(AlarmDaySet == 1)
                    {
                        String todaydata = meals[days].lunch;
                        if (todaydata != "급식이 없습니다.") {
                            Log.d("Alarm", "급식 알람 작동");
                            Notification.BigTextStyle meala = new Notification.BigTextStyle(
                                    new Notification.Builder(context)
                                            .setContentTitle("어람중학교 내일의 급식")
                                            .setContentText(todaydata)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setAutoCancel(true)
                                            .setVibrate(vibrate)
                                            .setTicker("어람중 내일의 급식은?"))
                                    .setSummaryText("어람중학교")
                                    .bigText(todaydata);
                            manager.notify(1, meala.build());
                        }
                    }
                    Log.d("Alarm", "급식 없음");
                }
            }
        }
    }
}
