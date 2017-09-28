package com.seoul.culture;

import com.seoul.culture.util.CircleFlowIndicator;
import com.seoul.culture.util.ImageAdapter;
import com.seoul.culture.util.ViewFlow;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ShowActivity extends Activity {

	private ViewFlow viewFlow;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(new ImageAdapter(this), 0);
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		indic.bringToFront();
		indic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("seoul", "click");
			}
		});
	}
	/* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}