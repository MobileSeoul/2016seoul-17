package com.seoul.culture;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragCategory extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.category, container, false);
		Button[] btn = new Button[4];// 운동, 문화, 어린이/청소년, 기타 순서

		btn[0] = (Button) root.findViewById(R.id.button1);
		btn[1] = (Button) root.findViewById(R.id.button2);
		btn[2] = (Button) root.findViewById(R.id.button3);
		btn[3] = (Button) root.findViewById(R.id.button4);
//		CoordinatorLayout cl = (CoordinatorLayout)root.findViewById(R.id.cl);
//		cl.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Log.d("seoul", "click");
//			}
//		});

		for (int i = 0; i < btn.length; i++) {
			final int type = i;
			btn[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), CenterDetailActivity.class);
					intent.putExtra("type", type);
					startActivity(intent);
				}
			});
		}
		return root;
	}

}
