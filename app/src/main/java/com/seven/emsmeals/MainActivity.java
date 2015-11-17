package com.seven.emsmeals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.seven.emsmeals.schloader;
import com.seven.emsmeals.mealloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    private Context mContext;
    long backPressedTime;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        SharedPreferences mealchk = getSharedPreferences("mealchk", MODE_MULTI_PROCESS);
        boolean MealChk = mealchk.getBoolean("mealchk", true);

        SharedPreferences schchk = getSharedPreferences("schchk", MODE_MULTI_PROCESS);
        boolean SchChk = schchk.getBoolean("schchk", true);

        if(MealChk == true || SchChk == true)
        {
            Log.d("MainActivity", "알람 서비스 시작");
            Intent intent = new Intent(this, AlarmService.class);
            startService(intent);
        }
        else
        {
            Log.d("MainActivity","서비스 정지");
            Intent intent = new Intent(this, AlarmService.class);
            stopService(intent);
        }

        // 급식 및 학사 일정 Download 등 처리
        Calendar cal = Calendar.getInstance();
        int month = cal.get ( cal.MONTH ) + 1 ;
        SharedPreferences data = getSharedPreferences("month", MODE_MULTI_PROCESS);
        int dmonth = data.getInt("month", 0);

        if(dmonth != month)
        {
            Log.d("MainActivity", "Data Downloading...");
            if(isNetworkAvailable() == true) {
                DataThread t = new DataThread(MainActivity.this);
                t.start();
                Toast.makeText(getApplicationContext(), getString(R.string.download_start), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.d("MainActivity", "Internet Connect Failed");
                Toast.makeText(getApplicationContext(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.homepage) {
            if(isNetworkAvailable() == true) {
                finish();
                Intent intent = new Intent(MainActivity.this, HPView.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.redownload) {
            if (isNetworkAvailable() == true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog.Builder builders = builder.setTitle("데이터 재설정")
                        .setMessage("데이터를 정말 재설정 하시겠습니까?\n에러가 나지 않는다면 하지 않으셔도 됩니다.")
                                .setCancelable(true)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.data_reset), Toast.LENGTH_SHORT).show();
                                        DataThread t = new DataThread(MainActivity.this);
                                        t.start();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.setting)
        {
            finish();
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        if(id == R.id.timetable)
        {
            finish();
            Intent intent = new Intent(MainActivity.this, TimeTableSet.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            switch(position) {
                case 0:
                    return new FirstActivity();
                case 1:
                    return new MealActivity();
                case 2:
                    return new SchActivity();
                case 3:
                    return new InfoActivity();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && 2000 >= intervalTime) super.onBackPressed();
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable()
    {
        ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connec.getNetworkInfo(0);
        NetworkInfo wifiInfo = connec.getNetworkInfo(1);
        NetworkInfo wimaxInfo = connec.getNetworkInfo(6);
        boolean bm = false;
        boolean bw = false;
        boolean bx = false;
        if (mobileInfo != null) bm = mobileInfo.isConnected();
        if (wimaxInfo != null) bx = wimaxInfo.isConnected();
        if (wifiInfo != null) bw = wifiInfo.isConnected();
        return (bm || bw || bx);
    }

}

class DataThread extends Thread
{
    Context Contexts;
    public DataThread(Context context) {
        Contexts = context;
    }

    public void run() {
        Log.d("Thread", "Thread Start!");
        Calendar cal = Calendar.getInstance();
        int year = cal.get ( cal.YEAR);
        int month = cal.get ( cal.MONTH ) + 1 ;
        int nextmonth = month + 1;

            try {
            Document sch = Jsoup.connect("http://hes.goe.go.kr/sts_sci_sf01_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03").get();
            Document meal = Jsoup.connect("http://hes.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03").get();
            Document mealnm = Jsoup.connect("http://hes.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03&schYm=" + year + "." + nextmonth).get();

            String schs = sch.toString();
            String meals = meal.toString();
            String mealnms = mealnm.toString();

            SharedPreferences schp = Contexts.getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor scheditor = schp.edit();
            scheditor.putString("sch", schs);
            scheditor.commit();

            SharedPreferences mealp = Contexts.getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor mealeditor = mealp.edit();
            mealeditor.putString("meal", meals);
            mealeditor.commit();

            SharedPreferences mealnmp = Contexts.getSharedPreferences("mealnm", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor mealnmeditor = mealnmp.edit();
            mealnmeditor.putString("mealnm", mealnms);
            mealnmeditor.commit();

            SharedPreferences pref = Contexts.getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("month", month);
            editor.commit();

            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            Log.d("Thread", "Thread Exception Error!");
            e.printStackTrace();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(Contexts, "다운로드가 완료되었습니다. 앱을 다시 실행시켜주세요.", Toast.LENGTH_LONG).show();
            handler.postDelayed(mMyTask, 3000);
        }
    };

    private Runnable mMyTask = new Runnable() {
        @Override
        public void run() {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };
}

