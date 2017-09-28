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
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;
import com.seoul.culture.CenterDetailActivity;
import com.seoul.culture.CenterInfoActivity;
import com.seoul.culture.R;
import com.seoul.culture.util.DataBaseHelper;
import com.seoul.culture.util.Seoul;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyMap extends NMapActivity implements OnMapStateChangeListener, OnMapViewTouchEventListener {

	String clientId = "9SIyg9SqyQeza4hpRq7c";
	NMapView mMapView = null;
	NMapController mMapController;
	OnMapStateChangeListener onMapViewStateChangeListener; // 지도 상태 변경 콜백 인터페이스
	OnMapViewTouchEventListener onMapViewTouchEventListener; // 지도 터치 이벤트 콜백
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	OnStateChangeListener onPOIdataStateChangeListener;
	NMapMyLocationOverlay mMyLocationOverlay; // 지도 위에 현재 위치를 표시하는 오버레이 클래스
	NMapLocationManager mMapLocationManager; // 단말기의 현재 위치 탐색 기능 사용 클래스
	NMapCompassManager mMapCompassManager; // 단말기의 나침반 기능 사용 클래스
	private String LOG_TAG = "seoul";
	final int DIALOG_MODE = 0;
	private ArrayList<Seoul> myseoul;
	private RelativeLayout mMapContainerView;
	private final Handler mHnadler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);
		mMapView = (NMapView)findViewById(R.id.mapView);

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

		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

		mMapView.setBuiltInZoomControls(true, null);

		mMapController = mMapView.getMapController();

		// 핀 관련
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		testOverlayMaker();

		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
		mMapCompassManager = new NMapCompassManager(this);
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

		final Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!mMapLocationManager.isMyLocationEnabled()){
					btn.setSelected(true);
					startMyLocation();
				} else {
					btn.setSelected(false);
					stopMyLocation();
				}
			}
		});

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
			mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 8);
		} else {
			Log.e("NMAP", "onMapInitHandler : error = " + errorInfo.toString());
		}
	}

	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private void testOverlayMaker() { // 오버레이 아이템 추가 함수
		int markerId = NMapPOIflagType.PIN; // 마커 id설정
		// POI 아이템 관리 클래스 생성(전체 아이템 수, NMapResourceProvider 상속 클래스)
		NMapPOIdata poiData = new NMapPOIdata(myseoul.size(), mMapViewerResourceProvider);
		poiData.beginPOIdata(myseoul.size()); // POI 아이템 추가 시작
		for (int i = 0; i < myseoul.size(); i++) {
			NGeoPoint np = new NGeoPoint();
			String[] temp = myseoul.get(i).nmap.split(",");
			np.latitude = Double.valueOf(temp[0]);
			np.longitude = Double.valueOf(temp[1]);
			poiData.addPOIitem(np, myseoul.get(i).name, markerId, 1);
		}
		poiData.endPOIdata(); // POI 아이템 추가 종료
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, myseoul.size());

//		poiDataOverlay.showAllPOIdata(0); // 모든 POI 데이터를 화면에 표시(zomLevel)
		poiDataOverlay.showAllPOIdata(9);
		poiDataOverlay.setOnStateChangeListener(new OnStateChangeListener() {
			public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
				showDialog(DIALOG_MODE);
				Log.d("seoul", item.toString()+"");
			}

			public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
				if (item != null) {
					Log.i(LOG_TAG , "onFocusChanged: " + item.toString());
				} else {
					Log.i(LOG_TAG, "onFocusChanged: ");
				}
			}
		});
	}

	// 위치 변경 콜백 인터페이스의 정의

	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() { // 위치
		// 위치가 변경되면 호출
		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
			if (mMapController != null) {
				mMapController.animateTo(myLocation); // 지도 중심을 현재 위치로 이동
			}
			return true;
		}

		// 정해진 시간 내에 위치 탐색 실패 시 호출
		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
		}

		// 현재 위치가 지도 상에 표시할 수 있는 범위를 벗어나는 경우 호출
		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
			stopMyLocation(); // 내 위치 찾기 중지 함수 호출
		}
	};

	// 내 위치 찾기 시작 함수 정의
	private void startMyLocation() {
		if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
			mOverlayManager.addOverlay(mMyLocationOverlay);
		}
		if (mMapLocationManager.isMyLocationEnabled()) { // 현재 위치를 탐색 중인지 확인
			if (!mMapView.isAutoRotateEnabled()) { // 지도 회전기능 활성화 상태 여부 확인
				mMyLocationOverlay.setCompassHeadingVisible(true); // 나침반 각도 표시
				mMapCompassManager.enableCompass(); // 나침반 모니터링 시작
				mMapView.setAutoRotateEnabled(true, false); // 지도 회전기능 활성화
				mMapContainerView.requestLayout();
				//				mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);

				mMapContainerView.requestLayout();
			} else {
				stopMyLocation();
			}
			mMapView.postInvalidate();
		} else { // 현재 위치를 탐색 중이 아니면
			Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false); // 현재
			// 시작
			if (!isMyLocationEnabled) { // 위치 탐색이 불가능하면
				Toast.makeText(MyMap.this, "GPS 서비스를 연결해 주세요",
						Toast.LENGTH_LONG).show();
				Intent goToSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(goToSettings);
				return;
			}
		}
	}

	// 내 위치 찾기 중지 함수 정의
	private void stopMyLocation() {
		mMapLocationManager.disableMyLocation(); // 현재 위치 탐색 종료
		if (mMapView.isAutoRotateEnabled()) { // 지도 회전기능이 활성화 상태라면
			mMyLocationOverlay.setCompassHeadingVisible(false); // 나침반 각도표시 제거
			mMapCompassManager.disableCompass(); // 나침반 모니터링 종료
			mMapView.setAutoRotateEnabled(false, false); // 지도 회전기능 중지
			mMapContainerView.requestLayout();
		}
	}

	protected Dialog onCreateDialog(int id, Bundle args) {
		if(id==DIALOG_MODE){
			AlertDialog.Builder dialog1 = new Builder(this);
			final String str1[] = { "강좌정보", "센터정보" };
			dialog1.setTitle("옵션 선택");
			dialog1.setItems(str1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					if(which == 0) {
						intent = new Intent(getApplicationContext(), CenterDetailActivity.class);
					} else {
						intent = new Intent(getApplicationContext(), CenterInfoActivity.class);
					}
					intent.putExtra("name", "");
					startActivity(intent);
				}
			});
			return dialog1.create();
		}
		return super.onCreateDialog(id, args);
	}

	private class MapContainerView extends ViewGroup {

		public MapContainerView(Context context) {
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			final int width = getWidth();
			final int height = getHeight();
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);
				final int childWidth = view.getMeasuredWidth();
				final int childHeight = view.getMeasuredHeight();
				final int childLeft = (width - childWidth) / 2;
				final int childTop = (height - childHeight) / 2;
				view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			}

			if (changed) {
				mOverlayManager.onSizeChanged(width, height);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
			int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
			int sizeSpecWidth = widthMeasureSpec;
			int sizeSpecHeight = heightMeasureSpec;

			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);

				if (view instanceof NMapView) {
					if (mMapView.isAutoRotateEnabled()) {
						int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
						sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
						sizeSpecHeight = sizeSpecWidth;
					}
				}

				view.measure(sizeSpecWidth, sizeSpecHeight);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}// end of class
