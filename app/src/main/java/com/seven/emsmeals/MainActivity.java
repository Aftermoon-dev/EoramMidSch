package com.seven.emsmeals;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends ActionBarActivity {
    Context mContext;
    long backPressedTime;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={ "메인", "급식", "학사일정" };
    int Numboftabs = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>")));
        actionBar.setDisplayShowHomeEnabled(true);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorAccent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        // 급식 및 학사 일정 Download 등 처리
        Calendar cal = Calendar.getInstance();
        int month = cal.get ( cal.MONTH ) + 1 ;

        SharedPreferences data = getSharedPreferences("month", MODE_MULTI_PROCESS);
        int dmonth = data.getInt("month", 0);

        SharedPreferences mdata = getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdata = mdata.getString("meal", "");

        SharedPreferences mnmdata = getSharedPreferences("mealnm", Context.MODE_MULTI_PROCESS);
        String mealnmdata = mnmdata.getString("mealnm", "");

        SharedPreferences sdata = getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        String schdata = sdata.getString("sch", "");

        SharedPreferences snmdata = getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
        String schnmdata = snmdata.getString("schnm", "");

        // 데이터를 다운받은 달이 다른지, 급식 및 학사일정의 데이터가 비어있는지 확인하고 다운로드
        if(dmonth != month | mealdata.equals("") | mealnmdata.equals("") | schdata.equals("") | schnmdata.equals(""))
        {
            Log.d("MainActivity", "Data Downloading...");
            if(isNetworkAvailable() == true) {

                DataThread t = new DataThread(MainActivity.this);
                t.start();
                Toast.makeText(MainActivity.this, getString(R.string.download_start), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.d("MainActivity", "Internet Connection Failed");
                Toast.makeText(MainActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }

        pager.setOffscreenPageLimit(3);


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
        if(id == R.id.homepage) {
            if (isNetworkAvailable() == true) {
                Uri uri = Uri.parse("http://www.eoram.ms.kr");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
                                Toast.makeText(MainActivity.this, getString(R.string.data_reset), Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        String nowdate = sdf.format(cal.getTime());
        int nowyear = Integer.parseInt(nowdate.substring(0, 4));
        int nowmonth = Integer.parseInt(nowdate.substring(4, 6));
        cal.add(cal.MONTH,+1);
        String nextdate = sdf.format(cal.getTime());
        int nextyear = Integer.parseInt(nextdate.substring(0, 4));
        String Snextmonth = nextdate.substring(4, 6);

        try {

            Document sch = Jsoup.connect("http://stu.goe.go.kr/sts_sci_sf01_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03").get();
            Document schnm = Jsoup.connect("http://stu.goe.go.kr/sts_sci_sf01_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03&ay=" + nextyear + "&mm=" + Snextmonth).get();
            Document meal = Jsoup.connect("http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03").get();
            Document mealnm = Jsoup.connect("http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005475&schulCrseScCode=3&schulKndScCode=03&ay=" + nextyear + "&mm=" + Snextmonth).get();
            Log.d("Thread", "THIS YEAR : " + nowyear + "THIS MONTH : " + nowmonth);
            Log.d("Thread", "NEXT YEAR : " + nextyear + "NEXT MONTH : " + Snextmonth);

            String schs = sch.toString();
            String schnms = schnm.toString();
            String meals = meal.toString();
            String mealnms = mealnm.toString();

            SharedPreferences schp = Contexts.getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor scheditor = schp.edit();
            scheditor.putString("sch", schs);
            scheditor.commit();

            SharedPreferences schnmp = Contexts.getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor schnmeditor = schnmp.edit();
            schnmeditor.putString("schnm", schnms);
            schnmeditor.commit();

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
            editor.putInt("month", nowmonth);
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
            Toast.makeText(Contexts, "다운로드가 완료되었습니다. 자동으로 재시작됩니다.", Toast.LENGTH_LONG).show();
            ((Activity)Contexts).finish();
            Intent intent = new Intent(Contexts, MainActivity.class);
            Contexts.startActivity(intent);
        }
    };
}

