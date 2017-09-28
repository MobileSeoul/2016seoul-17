package com.seoul.culture.util;

import java.util.ArrayList;

public class GuCenter{
	public String gu = "";
	public ArrayList<String> name = new ArrayList<String>();
	String[] arrGu = {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", 
			"금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", 
			"성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
	private ArrayList<GuCenter> myGC = new ArrayList<GuCenter>();

	public GuCenter() {
	}

	public GuCenter(String gu, ArrayList<String> name) {
		super();
		this.gu = gu;
		this.name = name;
	}

	public String getGu() {
		return gu;
	}

	public void setGu(String gu) {
		this.gu = gu;
	}

	public ArrayList<String> getName() {
		return name;
	}

	public void setName(ArrayList<String> name) {
		this.name = name;
	}

	public ArrayList<GuCenter> update(ArrayList<Seoul> myseoul){
		for (int i = 0; i < arrGu.length; i++) {
			ArrayList<String> center = new ArrayList<String>();
			for (int j = 0; j < myseoul.size(); j++) {
				if(myseoul.get(j).gu.equals(arrGu[i])){
					center.add(myseoul.get(j).name);
				}
			}
			myGC.add(new GuCenter(arrGu[i], center));
		}
		return myGC;
	}
	
}
