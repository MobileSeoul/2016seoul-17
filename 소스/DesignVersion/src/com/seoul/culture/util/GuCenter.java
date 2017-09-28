package com.seoul.culture.util;

import java.util.ArrayList;

public class GuCenter{
	public String gu = "";
	public ArrayList<String> name = new ArrayList<String>();
	String[] arrGu = {"������", "������", "���ϱ�", "������", "���Ǳ�", "������", "���α�", 
			"��õ��", "�����", "������", "���빮��", "���۱�", "������", "���빮��", "���ʱ�", "������", 
			"���ϱ�", "���ı�", "��õ��", "��������", "��걸", "����", "���α�", "�߱�", "�߶���"};
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
