package com.seven.emsmeals;

import java.util.Calendar;

/**
 * Created by 민재 on 2015-04-05.
 */
public class schloader {
    public static ScheduleData[] schload()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        ScheduleData[] sch = SchoolAPI.getMonthlySchedule(SchoolAPI.Country.GYEONGGI, "J100005475", SchoolAPI.SchoolType.MIDDLE, year, month);

        return sch;
    }
}
