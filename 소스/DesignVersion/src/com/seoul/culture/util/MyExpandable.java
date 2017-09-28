package com.seoul.culture.util;

import java.util.ArrayList;

import com.seoul.culture.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandable extends BaseExpandableListAdapter {
	ArrayList<ArrayList<String>> childList;
	ArrayList<String> groupList;
	LayoutInflater lif;

	public MyExpandable(Context context, ArrayList<String> groupList, ArrayList<ArrayList<String>> childList) {
		super();
		this.lif = LayoutInflater.from(context);
		this.groupList = groupList;
		this.childList = childList;
	}

	public String getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = lif.inflate(R.layout.db_child_row, null);
		}

		TextView tvChild = (TextView)convertView.findViewById(R.id.textView1);
		
		String[] temp = getChild(groupPosition, childPosition).split("#");
		
		SpannableStringBuilder sps = new SpannableStringBuilder(temp[0]);
		sps.setSpan(new AbsoluteSizeSpan(48), 5, temp[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sps.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), 5, temp[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		SpannableStringBuilder sps2 = new SpannableStringBuilder(temp[1]);
		sps2.setSpan(new AbsoluteSizeSpan(48), 5, temp[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sps2.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), 5, temp[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		SpannableStringBuilder sps4 = new SpannableStringBuilder(temp[2]);
		sps4.setSpan(new AbsoluteSizeSpan(48), 5, temp[2].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sps4.setSpan(new ForegroundColorSpan(Color.parseColor("#039BE5")), 5, temp[2].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		tvChild.setText(sps);
		tvChild.append("\n");
		tvChild.append(sps2);
		tvChild.append("\n");
		tvChild.append(sps4);
		return convertView;
	}



	public String getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	public int getGroupCount() {
		return groupList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = lif.inflate(R.layout.db_parent_row, null);
		}

		TextView tvGroup = (TextView) convertView.findViewById(R.id.textView1);
		tvGroup.setText(getGroup(groupPosition)+"");		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	} 
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
