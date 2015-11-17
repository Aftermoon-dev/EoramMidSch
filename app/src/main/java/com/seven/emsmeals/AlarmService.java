package com.seven.emsmeals;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by 민재 on 2015-04-08.
 */

public class AlarmService extends Service {

    boolean MealChk; // 급식 알람 스위치
    int MealHour; // 급식 알람 시간
    int MealMinute; // 급식 알람 분

    boolean SchChk; // 일정 알람 스위치
    int SchHour; // 일정 알람 시간
    int SchMinute; // 일정 알람 분

    private PackageReceiver pReceiver; // 앱 업데이트 서비스 재실행

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        releaseAlarm();
        //RegisterReceivers();
        Log.d("service","onStartCommand 실행");

        // 급식 알람 온 오프 체크
        SharedPreferences mealchk = getSharedPreferences("mealchk", MODE_MULTI_PROCESS);
        MealChk = mealchk.getBoolean("mealchk", true);

        // 학사일정 알람 온오프 체크
        SharedPreferences schchk = getSharedPreferences("schchk", MODE_MULTI_PROCESS);
        SchChk = schchk.getBoolean("schchk", true);

        AlarmManager Alarm = (AlarmManager)getSystemService(ALARM_SERVICE);

        if(MealChk == true)
        {
            SharedPreferences mealhour = getSharedPreferences("mealhour", MODE_MULTI_PROCESS);
            MealHour = mealhour.getInt("mealhour", 21);

            SharedPreferences mealminute = getSharedPreferences("mealminute", MODE_MULTI_PROCESS);
            MealMinute = mealminute.getInt("mealminute", 0);

            // 급식 알람 등록
            Log.d("service", "급식 알람 등록");
            Intent meal = new Intent("com.seven.emsmeals.mealalarm");
            Calendar MealCal = Calendar.getInstance();
            MealCal.setTimeInMillis(System.currentTimeMillis());
            MealCal.set(Calendar.HOUR_OF_DAY, MealHour);
            MealCal.set(Calendar.MINUTE, MealMinute);
            MealCal.set(Calendar.SECOND, 0);
            PendingIntent MealSender = PendingIntent.getBroadcast(this, 0, meal, 0);
            Alarm.setRepeating(AlarmManager.RTC_WAKEUP, MealCal.getTimeInMillis(), 24 * 60 * 60 * 1000, MealSender);
        }

        if(SchChk == true)
        {
            SharedPreferences schhour = getSharedPreferences("schhour", MODE_MULTI_PROCESS);
            SchHour = schhour.getInt("schhour", 21);

            SharedPreferences schminute = getSharedPreferences("schminute", MODE_MULTI_PROCESS);
            SchMinute = schminute.getInt("schminute", 0);

            // 일정 알람 등록
            Log.d("service", "일정 알람 등록");
            Intent sch = new Intent("com.seven.emsmeals.schalarm");
            Calendar SchCal = Calendar.getInstance();
            SchCal.setTimeInMillis(System.currentTimeMillis());
            SchCal.set(Calendar.HOUR_OF_DAY, SchHour);
            SchCal.set(Calendar.MINUTE, SchMinute);
            SchCal.set(Calendar.SECOND, 0);
            PendingIntent SchSender = PendingIntent.getBroadcast(this, 0, sch, 0);
            Alarm.setRepeating(AlarmManager.RTC_WAKEUP, SchCal.getTimeInMillis(), 24 * 60 * 60 * 1000, SchSender);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("service","onCreate 실행");
        releaseAlarm();
        pReceiver = new PackageReceiver();
        IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);
    }

    @Override
    public void onDestroy() {
        Log.d("service","onDestroy 실행");
        releaseAlarm();
        if(pReceiver != null)
            Log.d("service","PackageReceiver 제거");
        unregisterReceiver(pReceiver);
    }

    private void releaseAlarm(){
        Log.d("service","알람 제거");
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람 서비스 등록

        PendingIntent sender; // Sender (알람 후 동작 전송 )

        Intent meal = new Intent("com.seven.emsmeals.mealalarm");
        sender = PendingIntent.getBroadcast(this, 0, meal, 0);
        alarm.cancel(sender);
        sender.cancel();

        Intent sch = new Intent("com.seven.emsmeals.schalarm");
        sender = PendingIntent.getBroadcast(this, 1, sch, 0);
        alarm.cancel(sender);
        sender.cancel();
    }

}
