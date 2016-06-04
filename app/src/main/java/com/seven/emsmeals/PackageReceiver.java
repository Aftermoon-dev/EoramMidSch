package com.seven.emsmeals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by 민재 on 2015-04-08.
 */

public class PackageReceiver extends BroadcastReceiver {

    boolean MealChk, SchChk;
    @Override
    public void onReceive(Context context, Intent intent) {

        // 급식 알람 온 오프 체크
        SharedPreferences mealchk = context.getSharedPreferences("mealchk", Context.MODE_MULTI_PROCESS);
        MealChk = mealchk.getBoolean("mealchk", true);

        // 학사일정 알람 온오프 체크
        SharedPreferences schchk = context.getSharedPreferences("schchk", Context.MODE_MULTI_PROCESS);
        SchChk = schchk.getBoolean("schchk", true);

        String action = intent.getAction();

        if(action.equals(Intent.ACTION_PACKAGE_ADDED))
        {

        }
        else if(action.equals(Intent.ACTION_PACKAGE_REMOVED))
        {

        }
        else if(action.equals(Intent.ACTION_PACKAGE_REPLACED)) {

            if(MealChk == true || SchChk == true) {
                Intent i = new Intent(context, AlarmService.class);
                context.startService(i);
            }
        }
    }
}
