package com.seoul.culture.map;

import java.util.ArrayList;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;
import com.seoul.culture.R;
import com.seoul.culture.util.DataBaseHelper;
import com.seoul.culture.util.Seoul;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MiniMap extends NMapActivity implements OnMapStateChangeListener, OnMapViewTouchEventListener {

	 public static Context mContext;
	String clientId = "9SIyg9SqyQeza4hpRq7c";
	NMapView mMapView = null;
	NMapController mMapController;
	OnMapStateChangeListener onMapViewStateChangeListener; // ���� ���� ���� �ݹ� �������̽�
	OnMapViewTouchEventListener onMapViewTouchEventListener; // ���� ��ġ �̺�Ʈ �ݹ�
	// �������̽�
	LinearLayout MapContainer;
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	OnStateChangeListener onPOIdataStateChangeListener;
	NMapMyLocationOverlay mMyLocationOverlay; // ���� ���� ���� ��ġ�� ǥ���ϴ� �������� Ŭ����
	NMapLocationManager mMapLocationManager; // �ܸ����� ���� ��ġ Ž�� ��� ��� Ŭ����
	NMapCompassManager mMapCompassManager; // �ܸ����� ��ħ�� ��� ��� Ŭ����
	private String LOG_TAG = "seoul";
	final int DIALOG_MODE = 0;
	private ArrayList<Seoul> myseoul;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mini_map);

		mMapView = (NMapView)findViewById(R.id.mapView);
		mContext = this;

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		String name = pref.getString("map", "");

		// �ʿ��� ����Ʈ ��(ȭ�� ����)
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		if (TextUtils.isEmpty(name)) {
			name = "���򱸹�ü������";
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String selectedItem = extras.getString("name");
			Log.d("seoul", name+"�Դϴ�");
		}

		// ��ü ���� DB �ҷ�����
		DataBaseHelper dbHelper = new DataBaseHelper(this);
		dbHelper.openDataBase();
		myseoul = dbHelper.Get_SeoulDetails();

		mMapView.setClientId(clientId);
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();

		mMapView.setOnMapStateChangeListener(this);// ���� ���� ����� ȣ��Ǵ� �ݹ� �������̽� ����
		mMapView.setOnMapViewTouchEventListener(this); // �������� ��ġ �̺�Ʈ ó�� �� ȣ��Ǵ� �ݹ� �������̽� ����
		mMapView.setBuiltInAppControl(true);

		mMapController = mMapView.getMapController();

		// �� ����
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

	}// end of onCreate

	@Override
	public void onLongPress(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLongPressCanceled(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(NMapView arg0, MotionEvent arg1, MotionEvent arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleTapUp(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchDown(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchUp(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		if (errorInfo == null) {
			NGeoPoint np = new NGeoPoint();
			for (int i = 0; i < myseoul.size(); i++) {
				if(myseoul.get(i).name.equals(name)){
					np.latitude = Double.valueOf(myseoul.get(i).nmap.split(",")[0]);
					np.longitude = Double.valueOf(myseoul.get(i).nmap.split(",")[1]);
				}
			}
			mMapController.setMapCenter(np, 7);
		} else {
			Log.e("NMAP", "onMapInitHandler : error = " + errorInfo.toString());
		}
	}

	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	
	public void testOverlayMaker(String nmap, String name) { // �������� ������ �߰� �Լ�
		int markerId = NMapPOIflagType.PIN; // ��Ŀ id����
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1); // POI ������ �߰� ����
		NGeoPoint np = new NGeoPoint();
		String[] temp = nmap.split(",");
		np.latitude = Double.valueOf(temp[0]);
		np.longitude = Double.valueOf(temp[1]);
		poiData.addPOIitem(np, name, markerId, 1);
		poiData.endPOIdata(); // POI ������ �߰� ����
		// POI data overlay ��ü ����(���� ���� �������� �������� ������ �� �ִ� �������� Ŭ����)
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, 100);
		poiDataOverlay.showAllPOIdata(0); // ��� POI �����͸� ȭ�鿡 ǥ��(zomLevel)
		// POI �������� ���� ���� ���� �� ȣ��Ǵ� �ݹ� �������̽� ����
	}
	


}// end of class