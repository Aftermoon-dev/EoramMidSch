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
public class SchAlarm extends BroadcastReceiver {
    long[] vibrate = { 100, 1000 }; // 진동
    String todaydata;

    @Override
    public void onReceive(Context context, Intent arg1) {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(arg1.getAction().equals("com.seven.emsmeals.schalarm")) {
            Calendar cal = Calendar.getInstance();
            int days = cal.get(cal.DAY_OF_MONTH);
            int month = cal.get(cal.MONTH) + 1;
            int hour = cal.get(cal.HOUR_OF_DAY);
            int minute = cal.get(cal.MINUTE);


            SharedPreferences data = context.getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
            int dmonth = data.getInt("month", 0);

            SharedPreferences schhour = context.getSharedPreferences("schhour", Context.MODE_MULTI_PROCESS);
            int SchHour = schhour.getInt("schhour", 21);

            SharedPreferences schminute = context.getSharedPreferences("schminute", Context.MODE_MULTI_PROCESS);
            int SchMinute = schminute.getInt("schminute", 0);


            if (dmonth == month)
            {
                SharedPreferences schdata = context.getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
                String schdb = schdata.getString("sch", "");

                SharedPreferences alarmdayset = context.getSharedPreferences("alarmdayset", Context.MODE_MULTI_PROCESS);
                int AlarmDaySet = alarmdayset.getInt("alarmdayset", 1);

                Document sch = Jsoup.parse(schdb);
                ScheduleData[] schs = ScheduleDataParser.parse(sch);

                if(hour == SchHour && minute == SchMinute ) {
                    if(AlarmDaySet == 0)
                    {
                        String todaydata = schs[days - 1].schedule;
                        if (todaydata != "일정이 없습니다.") {
                            Log.d("Alarm", "일정 알람 작동");
                            Notification.BigTextStyle scha = new Notification.BigTextStyle(
                                    new Notification.Builder(context)
                                            .setContentTitle("어람중학교 오늘의 일정")
                                            .setContentText(todaydata)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setAutoCancel(true)
                                            .setVibrate(vibrate)
                                            .setTicker("어람중 오늘의 일정은?"))
                                    .setSummaryText("어람중학교")
                                    .bigText(todaydata);
                            manager.notify(2, scha.build());
                        }
                    }
                    else if(AlarmDaySet == 1)
                    {
                        String todaydata = schs[days].schedule;
                        if (todaydata != "일정이 없습니다.") {
                            Log.d("Alarm", "일정 알람 작동");
                            Notification.BigTextStyle scha = new Notification.BigTextStyle(
                                    new Notification.Builder(context)
                                            .setContentTitle("어람중학교 내일의 일정")
                                            .setContentText(todaydata)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setAutoCancel(true)
                                            .setVibrate(vibrate)
                                            .setTicker("어람중 내일의 일정은?"))
                                    .setSummaryText("어람중학교")
                                    .bigText(todaydata);
                            manager.notify(2, scha.build());
                        }
                    }
                }
            }
        }
    }
}