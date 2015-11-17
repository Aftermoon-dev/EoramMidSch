package com.seven.emsmeals;

import java.util.Calendar;

/**
 * Created by 민재 on 2015-04-05.
 */
public class mealloader {
    public static MenuData[] mealload()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        MenuData[] meal = SchoolAPI.getMonthlyMenu(SchoolAPI.Country.GYEONGGI, "J100005475", SchoolAPI.SchoolType.MIDDLE, year, month);

        return meal;
}
        }
