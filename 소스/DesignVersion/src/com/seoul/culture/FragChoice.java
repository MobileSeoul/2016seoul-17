package com.seoul.culture;

import java.util.ArrayList;

import com.seoul.culture.util.DataBaseHelper;
import com.seoul.culture.util.MyGrid;
import com.seoul.culture.util.Seoul;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragChoice extends Fragment{
	private DataBaseHelper dbHelper;
	private ArrayList<Seoul> myseoul;
	private int type;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.choice, container, false);

//		// 전체 센터 DB 불러오기
		dbHelper = new DataBaseHelper(getActivity());
		dbHelper.openDataBase();
		myseoul = dbHelper.Get_SeoulDetails();
		
		GridView gv = (GridView)root.findViewById(R.id.gv);
		gv.setAdapter(new MyGrid(getActivity(), R.layout.grid_row, myseoul));
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), CenterInfoActivity.class);
				intent.putExtra("name", myseoul.get(position).name);
				
				startActivity(intent);
			}
		});
		return root;
	}
}
