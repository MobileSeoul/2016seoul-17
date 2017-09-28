package com.seoul.culture;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.seoul.culture.util.CSVFile;
import com.seoul.culture.util.Center;
import com.seoul.culture.util.DataBaseHelper;
import com.seoul.culture.util.Seoul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity{
	SearchView searchView;
	private ArrayList<Center> all;
	private CoordinatorLayout cl;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setHomeButtonEnabled(true);

		cl = (CoordinatorLayout)findViewById(R.id.cl);

		Intent intent = getIntent();
		String keyword = intent.getStringExtra("keyword");

		Log.d("seoul", "�˻��� = "+keyword);

		TextView tv = (TextView)findViewById(R.id.aa);
		SpannableStringBuilder sps = new SpannableStringBuilder(keyword);
		sps.setSpan(new AbsoluteSizeSpan(45), 0, keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sps.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), 0, keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		tv.setText("�˻��� = ");
		tv.append(sps);

		// ��� ���� �ҷ�����(�˻� ������)

		DataBaseHelper dbHelper = new DataBaseHelper(this);
		dbHelper.openDataBase();
		ArrayList<Seoul> myseoul = dbHelper.Get_SeoulDetails();

		ArrayList<String> arrResult = new ArrayList<String>();

		try {
			InputStream inputStream = this.getAssets().open("seouldata.csv");
			CSVFile csvFile = new CSVFile(inputStream);
			all = new ArrayList<Center>();
			all = csvFile.read();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < all.size(); i++) {
			Center temp = all.get(i);
			if(temp.center.contains(keyword) || temp.classname.contains(keyword)){
				arrResult.add(temp.center + "#" + temp.cafegory + "#" + temp.classname);
			} 
		}

		//		 �ߺ� ����
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < arrResult.size(); i++) {
			for (int j = 0; j < FOCUSED_STATE_SET.length; j++) {
				tempList.add(arrResult.get(i));
			}
		}
		final ArrayList<String> dataList = new ArrayList<String>(new HashSet<String>(tempList));
		Collections.sort(dataList);

		Log.d("seoul", arrResult.size()+"");
		if(arrResult.size() == 0 ){
			Log.d("seoul", "����");
			Snackbar.make(cl, "�˻� ����� �����ϴ�.\n�ٸ� �˻�� �Է����ּ���", Snackbar.LENGTH_LONG).show();
		} 
		
		ListView lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(new ListAdapter(getApplicationContext(), R.layout.search_row, dataList));
		lv.setOnItemClickListener(new OnItemClickListener() {
			private int type;

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final int temp = position;
				String aa = dataList.get(temp).split("#")[1];
				type = 0;
				if("�".equals(aa)){
					type = 0;
				} else if("��ȭ".equals(aa)){
					type = 1;
				} else if("����,û�ҳ�".equals(aa)){
					type = 2;
				} else if("��Ÿ".equals(aa)){
					type = 3;
				}
				
				Snackbar.make(cl, "���� ������ Ȯ���Ͻðڽ��ϱ�?", Snackbar.LENGTH_LONG)
				.setAction("Ȯ��", new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), CenterDetailActivity.class);
						intent.putExtra("name", dataList.get(temp).split("#")[0]);
						intent.putExtra("type", type);
						startActivity(intent);
						finish();
					}
				}).show();
			}
		});
	}//end of class

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class ListAdapter extends BaseAdapter {
		int row;
		ArrayList<String> myseoul;
		LayoutInflater lif;

		public ListAdapter(Context context, int row, ArrayList<String> myseoul) {
			this.row = row;
			this.myseoul = myseoul;
			this.lif = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return myseoul.size();
		}

		public Object getItem(int position) {
			return myseoul.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = lif.inflate(row, null);
			}

			TextView tvFirst = (TextView)convertView.findViewById(R.id.textView1);
			TextView tvSecond = (TextView)convertView.findViewById(R.id.textView2);
			TextView tvThird = (TextView)convertView.findViewById(R.id.textView3);
			tvFirst.setText(myseoul.get(position).split("#")[0]);
			tvSecond.setText(myseoul.get(position).split("#")[1]);
			tvThird.setText(myseoul.get(position).split("#")[2]);

			return convertView;
		}

	}

}
