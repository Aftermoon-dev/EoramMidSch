package com.seven.emsmeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by 민재 on 2015-11-19.
 */
public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(HelpActivity.this, SettingActivity.class);
        startActivity(intent);
        return;
    }
}