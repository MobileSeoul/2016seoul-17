package com.seoul.culture.util;

import java.util.ArrayList;

import com.seoul.culture.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyList extends BaseAdapter {
	int row;
	ArrayList<Info> myseoul;
	LayoutInflater lif;

	public MyList(Context context, int row, ArrayList<Info> myseoul) {
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

		TextView tvHead = (TextView)convertView.findViewById(R.id.textView1);
		TextView tvSecond = (TextView)convertView.findViewById(R.id.textView2);
		tvHead.setText(myseoul.get(position).head);
		tvSecond.setText(myseoul.get(position).detail);
		
		return convertView;
	}



}