package com.seoul.culture.util;

import java.util.ArrayList;

import com.seoul.culture.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MySpinner implements SpinnerAdapter {
	ArrayList<String> mygc;
	LayoutInflater lif;
	int row;

	public MySpinner(Context context, int row, ArrayList<String> mygc) {
		this.row = row;
		this.mygc = mygc;
		lif = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount(){
		return mygc.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = lif.inflate(row, null);
		}

		TextView tvGu = (TextView)convertView.findViewById(R.id.textView1);
		tvGu.setText(mygc.get(position));

		return convertView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public Object getItem(int position) {
		return mygc.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = lif.inflate(row, null);
		}

		TextView tvGu = (TextView)convertView.findViewById(R.id.textView1);
		tvGu.setText(mygc.get(position));
		return convertView;
	}

}