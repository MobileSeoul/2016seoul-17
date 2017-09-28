package com.seoul.culture.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.seoul.culture.R;

import android.app.Activity;

public class Center extends Activity{
	public String center;
	public String cafegory;
	public String classname;
	public String day;
	public String time;
	public String price;

	public Center() {
	}

	public Center(String center, String cafegory, String classname, String day, String time, String price) {
		super();
		this.center = center;
		this.cafegory = cafegory;
		this.classname = classname;
		this.day = day;
		this.time = time;
		this.price = price;
	}

	public ArrayList<Center> typeHealth(ArrayList<Center> all, String center) {
		ArrayList<Center> temp = new ArrayList<Center>();
		for (int i = 0; i < all.size(); i++) {
			if(center.equals(all.get(i).center)){
				if("운동".equals(all.get(i).cafegory)){
					temp.add(all.get(i));
				}

			}
		}
		return temp;
	}
	public ArrayList<Center> typeCulture(ArrayList<Center> all, String center) {
		ArrayList<Center> temp = new ArrayList<Center>();
		for (int i = 0; i < all.size(); i++) {
			if(center.equals(all.get(i).center)){
				if("문화".equals(all.get(i).cafegory)){
					temp.add(all.get(i));
				}

			}
		}
		return temp;
	}
	public ArrayList<Center> typeYouth(ArrayList<Center> all, String center) {
		ArrayList<Center> temp = new ArrayList<Center>();
		for (int i = 0; i < all.size(); i++) {
			if(center.equals(all.get(i).center)){
				if("유아,청소년".equals(all.get(i).cafegory)){
					temp.add(all.get(i));
				}
			}
		}
		return temp;
	}
	public ArrayList<Center> typeOthers(ArrayList<Center> all, String center) {
		ArrayList<Center> temp = new ArrayList<Center>();
		for (int i = 0; i < all.size(); i++) {
			if(center.equals(all.get(i).center)){
				if("기타".equals(all.get(i).cafegory)){
					temp.add(all.get(i));
				}
			}
		}
		return temp;
	}

}
