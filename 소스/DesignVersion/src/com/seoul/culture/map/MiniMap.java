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
	OnMapStateChangeListener onMapViewStateChangeListener; // 지도 상태 변경 콜백 인터페이스
	OnMapViewTouchEventListener onMapViewTouchEventListener; // 지도 터치 이벤트 콜백
	// 인터페이스
	LinearLayout MapContainer;
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	OnStateChangeListener onPOIdataStateChangeListener;
	NMapMyLocationOverlay mMyLocationOverlay; // 지도 위에 현재 위치를 표시하는 오버레이 클래스
	NMapLocationManager mMapLocationManager; // 단말기의 현재 위치 탐색 기능 사용 클래스
	NMapCompassManager mMapCompassManager; // 단말기의 나침반 기능 사용 클래스
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

		// 필요한 인텐트 값(화면 구성)
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		if (TextUtils.isEmpty(name)) {
			name = "은평구민체육센터";
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String selectedItem = extras.getString("name");
			Log.d("seoul", name+"입니다");
		}

		// 전체 센터 DB 불러오기
		DataBaseHelper dbHelper = new DataBaseHelper(this);
		dbHelper.openDataBase();
		myseoul = dbHelper.Get_SeoulDetails();

		mMapView.setClientId(clientId);
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();

		mMapView.setOnMapStateChangeListener(this);// 지도 상태 변경시 호출되는 콜백 인터페이스 설정
		mMapView.setOnMapViewTouchEventListener(this); // 지도에서 터치 이벤트 처리 후 호출되는 콜백 인터페이스 설정
		mMapView.setBuiltInAppControl(true);

		mMapController = mMapView.getMapController();

		// 핀 관련
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

	
	public void testOverlayMaker(String nmap, String name) { // 오버레이 아이템 추가 함수
		int markerId = NMapPOIflagType.PIN; // 마커 id설정
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1); // POI 아이템 추가 시작
		NGeoPoint np = new NGeoPoint();
		String[] temp = nmap.split(",");
		np.latitude = Double.valueOf(temp[0]);
		np.longitude = Double.valueOf(temp[1]);
		poiData.addPOIitem(np, name, markerId, 1);
		poiData.endPOIdata(); // POI 아이템 추가 종료
		// POI data overlay 객체 생성(여러 개의 오버레이 아이템을 포함할 수 있는 오버레이 클래스)
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, 100);
		poiDataOverlay.showAllPOIdata(0); // 모든 POI 데이터를 화면에 표시(zomLevel)
		// POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
	}
	


}// end of class