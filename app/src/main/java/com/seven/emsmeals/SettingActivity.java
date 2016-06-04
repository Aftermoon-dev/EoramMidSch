package com.seven.emsmeals;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;


public class SettingActivity extends ActionBarActivity {
    boolean MealChk; // 급식 알림 여부
    boolean SchChk; // 일정 알림 여부
    int MealHour; // 급식 알림 시간
    int MealMinute; // 급식 알림 분
    int SchHour; // 일정 알림 시간
    int SchMinute; // 일정 알림 분
    int AlarmDaySet; // 오늘 기준 알림인지, 내일 기준 알림인지 설정.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        CheckBox MealChks = (CheckBox) findViewById(R.id.mealchk); // 급식알림 온오프 체크박스
        CheckBox SchChks = (CheckBox) findViewById(R.id.schchk); // 일정알림 온오프 체크박스
        final CheckBox TodayAlarmSet = (CheckBox) findViewById(R.id.todayalarmset);
        final CheckBox TomoAlarmSet = (CheckBox) findViewById(R.id.tomoalarmset);
        CardView mealtimeset = (CardView) findViewById(R.id.MealTimeSet);
        CardView schtimeset = (CardView) findViewById(R.id.SchTimeSet);
        CardView timetableset = (CardView) findViewById(R.id.TimeTableSet);
        CardView help = (CardView) findViewById(R.id.HelpCard);

        getPreferences();

        mealtimeset.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SettingActivity.this, MealTimeSetListener, MealHour, MealMinute, false).show();
            }
        });

        schtimeset.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SettingActivity.this, SchTimeSetListener, SchHour, SchMinute, false).show();
            }
        });

        timetableset.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(SettingActivity.this, TimeTableSet.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        if(MealChk == true)
        {
            MealChks.setChecked(true);
        }
        else
        {
            MealChks.setChecked(false);
        }

        if(SchChk == true)
        {
            SchChks.setChecked(true);
        }
        else
        {
            SchChks.setChecked(false);
        }

        if(AlarmDaySet == 0)
        {
            TodayAlarmSet.setChecked(true);
            TomoAlarmSet.setChecked(false);
        }
        else if(AlarmDaySet == 1)
        {
            TodayAlarmSet.setChecked(false);
            TomoAlarmSet.setChecked(true);
        }

        MealChks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                MealChk = isChecking;
            }
        });

        SchChks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                SchChk = isChecking;
            }
        });

        TodayAlarmSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                if(isChecking == true)
                {
                    // 0은 오늘 1은 내일
                    AlarmDaySet = 0;
                    TomoAlarmSet.setChecked(false);
                }
                else
                {
                    AlarmDaySet = 1;
                    TomoAlarmSet.setChecked(true);
                }
            }
        });

        TomoAlarmSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                if(isChecking == true)
                {
                    AlarmDaySet = 1;
                    TodayAlarmSet.setChecked(false);
                }
                else
                {
                    AlarmDaySet = 0;
                    TodayAlarmSet.setChecked(true);
                }
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener MealTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            MealHour = hourOfDay;
            MealMinute = minute;
        }
    };

    private TimePickerDialog.OnTimeSetListener SchTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SchHour = hourOfDay;
            SchMinute = minute;
        }
    };


    public void onBackPressed() {
        MealSPSave(MealHour, MealMinute);
        SchSPSave(SchHour, SchMinute);
        AlarmDaySPSave(AlarmDaySet);
        ServiceSetting();
        CheckSave();
        finish();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        return;
    }

    public void getPreferences()
    {
        SharedPreferences mealchk = getSharedPreferences("mealchk", MODE_MULTI_PROCESS);
        MealChk = mealchk.getBoolean("mealchk", true);

        SharedPreferences schchk = getSharedPreferences("schchk", MODE_MULTI_PROCESS);
        SchChk = schchk.getBoolean("schchk", true);

        SharedPreferences alarmdayset = getSharedPreferences("alarmdayset", MODE_MULTI_PROCESS);
        AlarmDaySet = alarmdayset.getInt("alarmdayset", 1);

        SharedPreferences mealhour = getSharedPreferences("mealhour", MODE_MULTI_PROCESS);
        MealHour = mealhour.getInt("mealhour", 21);

        SharedPreferences mealminute = getSharedPreferences("mealminute", MODE_MULTI_PROCESS);
        MealMinute = mealminute.getInt("mealminute", 0);

        SharedPreferences schhour = getSharedPreferences("schhour", MODE_MULTI_PROCESS);
        SchHour = schhour.getInt("schhour", 21);

        SharedPreferences schminute = getSharedPreferences("schminute", MODE_MULTI_PROCESS);
        SchMinute = schminute.getInt("schminute", 0);
    }

    public void CheckSave()
    {
        SharedPreferences mealchk = getSharedPreferences("mealchk", MODE_MULTI_PROCESS);
        SharedPreferences.Editor mealchkedit = mealchk.edit();
        mealchkedit.putBoolean("mealchk", MealChk);
        mealchkedit.commit();

        SharedPreferences schchk = getSharedPreferences("schchk", MODE_MULTI_PROCESS);
        SharedPreferences.Editor schchkedit = schchk.edit();
        schchkedit.putBoolean("schchk", SchChk);
        schchkedit.commit();
    }

    public void AlarmDaySPSave(int value)
    {
        SharedPreferences alarmt = getSharedPreferences("alarmdayset", MODE_MULTI_PROCESS);
        SharedPreferences.Editor alarmdayedit = alarmt.edit();
        alarmdayedit.putInt("alarmdayset", value);
        alarmdayedit.commit();
    }


    public void MealSPSave(int hourOfDay, int minute)
    {
        SharedPreferences mealhour = getSharedPreferences("mealhour", MODE_MULTI_PROCESS);
        SharedPreferences.Editor medit = mealhour.edit();
        medit.putInt("mealhour", hourOfDay);
        medit.commit();

        SharedPreferences mealminute = getSharedPreferences("mealminute", MODE_MULTI_PROCESS);
        SharedPreferences.Editor mmedit = mealminute.edit();
        mmedit.putInt("mealminute", minute);
        mmedit.commit();
    }

    public void SchSPSave(int hourOfDay, int minute)
    {
        SharedPreferences schhour = getSharedPreferences("schhour", MODE_MULTI_PROCESS);
        SharedPreferences.Editor sedit = schhour.edit();
        sedit.putInt("schhour", hourOfDay);
        sedit.commit();

        SharedPreferences schminute = getSharedPreferences("schminute", MODE_MULTI_PROCESS);
        SharedPreferences.Editor smedit = schminute.edit();
        smedit.putInt("schminute", minute);
        smedit.commit();
    }


    public void ServiceSetting() {

        if(MealChk == true || SchChk == true)
        {
            Log.d("SettingActivity", "서비스 시작");
            Intent intent = new Intent("com.seven.emsmeals.alarmservice");
            intent.setPackage("com.seven.emsmeals");
            startService(intent);

        }
        else
        {
            Log.d("SettingActivity","서비스 정지");
            Intent intent = new Intent("com.seven.emsmeals.alarmservice");
            intent.setPackage("com.seven.emsmeals");
            stopService(intent);
        }
    }
}
