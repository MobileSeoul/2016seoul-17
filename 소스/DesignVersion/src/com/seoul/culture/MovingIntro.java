package com.seoul.culture;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MovingIntro extends Activity{
	private StringBuilder info;
	private Timer timer;
	Handler handler = new Handler();
	private String xml;
	ArrayList<String> name = new ArrayList<String>();
	String array = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movingintro);

		AnimationDrawable ad = (AnimationDrawable)findViewById(R.id.imageView1).getBackground();
		ad.start();

		final SharedPreferences sp = getSharedPreferences("bookmark", MODE_PRIVATE);
		final boolean start = sp.getBoolean("start", true);

		TimerTask task=new TimerTask() {
			public void run() {//실행할 작업을 기술.
				Intent intent = null;
				if(start){
					Editor editor = sp.edit();
					editor.putBoolean("start", false);
					Log.d("seoul", "초기시작");
					editor.commit();
					intent = new Intent(getApplicationContext(), ShowActivity.class);
				} else {
					intent = new Intent(getApplicationContext(),MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		};
		timer =new Timer();
		timer.schedule(task, 2000);

	}
	@Override
	protected void onPause() {
		super.onPause();
		timer.cancel();
		finish();
	}

}//end of class
