package com.seven.emsmeals;

/**
 * Copyright 2014 Seven(S.M.J). All rights reserved.
 * 
 * @author ddol0225@naver.com
 * @version 1.0.0
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Runnable r = new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {

				}
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				SplashActivity.this.finish();
			}
		};
		new Thread(r).start();
	}

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
