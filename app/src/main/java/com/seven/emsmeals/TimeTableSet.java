package com.seven.emsmeals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class TimeTableSet extends ActionBarActivity {
    String MonTables, TueTables, WedTables, ThuTables, FriTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_set);
        EditText Mon = (EditText) findViewById(R.id.montable);
        EditText Tue = (EditText) findViewById(R.id.tuetable);
        EditText Wed = (EditText) findViewById(R.id.wedtable);
        EditText Thu = (EditText) findViewById(R.id.thutable);
        EditText Fri = (EditText) findViewById(R.id.fritable);
        GetData();
        Mon.setText(MonTables);
        Tue.setText(TueTables);
        Wed.setText(WedTables);
        Thu.setText(ThuTables);
        Fri.setText(FriTables);

        Mon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                MonTables = s.toString();
            }
        });

        Tue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                TueTables = s.toString();
            }
        });

        Wed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                WedTables = s.toString();
            }
        });

        Thu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                ThuTables = s.toString();
            }
        });
        Fri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                FriTables = s.toString();
            }
        });
    }

    public void GetData()
    {
        SharedPreferences montable = getSharedPreferences("montable", MODE_MULTI_PROCESS);
        MonTables = montable.getString("montable", getString(R.string.notime));

        SharedPreferences tuetable = getSharedPreferences("tuetable", MODE_MULTI_PROCESS);
        TueTables = tuetable.getString("tuetable", getString(R.string.notime));

        SharedPreferences wedtable = getSharedPreferences("wedtable", MODE_MULTI_PROCESS);
        WedTables = wedtable.getString("wedtable", getString(R.string.notime));

        SharedPreferences thutable = getSharedPreferences("thutable", MODE_MULTI_PROCESS);
        ThuTables = thutable.getString("thutable", getString(R.string.notime));

        SharedPreferences fritable = getSharedPreferences("fritable", MODE_MULTI_PROCESS);
        FriTables = fritable.getString("fritable", getString(R.string.notime));
    }

    public void SaveData()
    {
        SharedPreferences MonTable = getSharedPreferences("montable", MODE_MULTI_PROCESS);
        SharedPreferences.Editor MonTableEdit = MonTable.edit();
        MonTableEdit.putString("montable", MonTables);
        MonTableEdit.commit();

        SharedPreferences TueTable = getSharedPreferences("tuetable", MODE_MULTI_PROCESS);
        SharedPreferences.Editor TueTableEdit = TueTable.edit();
        TueTableEdit.putString("tuetable", TueTables);
        TueTableEdit.commit();

        SharedPreferences WedTable = getSharedPreferences("wedtable", MODE_MULTI_PROCESS);
        SharedPreferences.Editor WedTableEdit = WedTable.edit();
        WedTableEdit.putString("wedtable", WedTables);
        WedTableEdit.commit();

        SharedPreferences ThuTable = getSharedPreferences("thutable", MODE_MULTI_PROCESS);
        SharedPreferences.Editor ThuTableEdit = ThuTable.edit();
        ThuTableEdit.putString("thutable", ThuTables);
        ThuTableEdit.commit();

        SharedPreferences FriTable = getSharedPreferences("fritable", MODE_MULTI_PROCESS);
        SharedPreferences.Editor FriTableEdit = FriTable.edit();
        FriTableEdit.putString("fritable", FriTables);
        FriTableEdit.commit();
    }

    public void onBackPressed() {
        Log.d("timetable", "onBackPressed");
        SaveData();
        finish();
        Intent intent = new Intent(TimeTableSet.this, SettingActivity.class);
        startActivity(intent);
        return;
    }
}
