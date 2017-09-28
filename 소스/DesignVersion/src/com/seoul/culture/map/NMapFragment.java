/*
 * Copyright 2016 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seoul.culture.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.nmapmodel.NMapError;

/** 
 * NMapFragment Ŭ������ NMapActivity�� ������� �ʰ� NMapView�� ����ϰ��� �ϴ� ��쿡 NMapContext�� �̿��� ������.
 * NMapView ���� �ʿ��� �ʱ�ȭ �� ������ ����� NMapActivity ���ÿ� ������.
 */
public class NMapFragment extends Fragment {

	private NMapContext mMapContext;
	
	/**
	 * Fragment�� ���Ե� NMapView ��ü�� ��ȯ��
	 */
	private NMapView findMapView(View v) {

	    if (!(v instanceof ViewGroup)) {
	        return null;
	    }

	    ViewGroup vg = (ViewGroup)v;
	    if (vg instanceof NMapView) {
	        return (NMapView)vg;
	    }
	    
	    for (int i = 0; i < vg.getChildCount(); i++) {

	        View child = vg.getChildAt(i);
		    if (!(child instanceof ViewGroup)) {
		        continue;
		    }
		    
		    NMapView mapView = findMapView(child);
		    if (mapView != null) {
		    	return mapView;
		    }
	    }
	    return null;
	}

	/* Fragment ����������Ŭ�� ���� NMapContext�� �ش� API�� ȣ���� */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mMapContext =  new NMapContext(super.getActivity()); 
	    
	    mMapContext.onCreate();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		throw new IllegalArgumentException("onCreateView should be implemented in the subclass of NMapFragment.");
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    // Fragment�� ���Ե� NMapView ��ü ã��
	    NMapView mapView = findMapView(super.getView());
	    if (mapView == null) {
	    	throw new IllegalArgumentException("NMapFragment dose not have an instance of NMapView.");
	    }
	    
	    // NMapActivity�� ������� �ʴ� ��쿡�� NMapView ��ü ������ �ݵ�� setupMapView()�� ȣ���ؾ���.
	    mMapContext.setupMapView(mapView);
	}
	
	@Override
	public void onStart(){
	    super.onStart();
	    
	    mMapContext.onStart();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    mMapContext.onResume();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    
	    mMapContext.onPause();
	}
	
	@Override
	public void onStop() {
		mMapContext.onStop();
		
	    super.onStop();
	}
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		mMapContext.onDestroy();
		
	    super.onDestroy();
	}

}
