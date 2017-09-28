package com.seoul.culture.util;

import com.seoul.culture.R;
import com.wx.wheelview.adapter.BaseWheelAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyWheel extends BaseWheelAdapter<String> {

	private Context mContext;

	public MyWheel(Context context) {
		mContext = context;
	}

	@Override
	protected View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.wheel_view, null);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(mList.get(position));
		return convertView;
	}

	static class ViewHolder {
		TextView textView;
	}

}