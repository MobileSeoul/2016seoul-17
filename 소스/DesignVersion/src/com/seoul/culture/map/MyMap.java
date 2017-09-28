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
	OnMapStateChangeListener onMapViewStateChangeListener; // ���� ���� ���� �ݹ� �������̽�
	OnMapViewTouchEventListener onMapViewTouchEventListener; // ���� ��ġ �̺�Ʈ �ݹ�
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	OnStateChangeListener onPOIdataStateChangeListener;
	NMapMyLocationOverlay mMyLocationOverlay; // ���� ���� ���� ��ġ�� ǥ���ϴ� �������� Ŭ����
	NMapLocationManager mMapLocationManager; // �ܸ����� ���� ��ġ Ž�� ��� ��� Ŭ����
	NMapCompassManager mMapCompassManager; // �ܸ����� ��ħ�� ��� ��� Ŭ����
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

		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

		mMapView.setBuiltInZoomControls(true, null);

		mMapController = mMapView.getMapController();

		// �� ����
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

	private void testOverlayMaker() { // �������� ������ �߰� �Լ�
		int markerId = NMapPOIflagType.PIN; // ��Ŀ id����
		// POI ������ ���� Ŭ���� ����(��ü ������ ��, NMapResourceProvider ��� Ŭ����)
		NMapPOIdata poiData = new NMapPOIdata(myseoul.size(), mMapViewerResourceProvider);
		poiData.beginPOIdata(myseoul.size()); // POI ������ �߰� ����
		for (int i = 0; i < myseoul.size(); i++) {
			NGeoPoint np = new NGeoPoint();
			String[] temp = myseoul.get(i).nmap.split(",");
			np.latitude = Double.valueOf(temp[0]);
			np.longitude = Double.valueOf(temp[1]);
			poiData.addPOIitem(np, myseoul.get(i).name, markerId, 1);
		}
		poiData.endPOIdata(); // POI ������ �߰� ����
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, myseoul.size());

//		poiDataOverlay.showAllPOIdata(0); // ��� POI �����͸� ȭ�鿡 ǥ��(zomLevel)
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

	// ��ġ ���� �ݹ� �������̽��� ����

	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() { // ��ġ
		// ��ġ�� ����Ǹ� ȣ��
		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
			if (mMapController != null) {
				mMapController.animateTo(myLocation); // ���� �߽��� ���� ��ġ�� �̵�
			}
			return true;
		}

		// ������ �ð� ���� ��ġ Ž�� ���� �� ȣ��
		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
		}

		// ���� ��ġ�� ���� �� ǥ���� �� �ִ� ������ ����� ��� ȣ��
		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
			stopMyLocation(); // �� ��ġ ã�� ���� �Լ� ȣ��
		}
	};

	// �� ��ġ ã�� ���� �Լ� ����
	private void startMyLocation() {
		if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
			mOverlayManager.addOverlay(mMyLocationOverlay);
		}
		if (mMapLocationManager.isMyLocationEnabled()) { // ���� ��ġ�� Ž�� ������ Ȯ��
			if (!mMapView.isAutoRotateEnabled()) { // ���� ȸ����� Ȱ��ȭ ���� ���� Ȯ��
				mMyLocationOverlay.setCompassHeadingVisible(true); // ��ħ�� ���� ǥ��
				mMapCompassManager.enableCompass(); // ��ħ�� ����͸� ����
				mMapView.setAutoRotateEnabled(true, false); // ���� ȸ����� Ȱ��ȭ
				mMapContainerView.requestLayout();
				//				mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);

				mMapContainerView.requestLayout();
			} else {
				stopMyLocation();
			}
			mMapView.postInvalidate();
		} else { // ���� ��ġ�� Ž�� ���� �ƴϸ�
			Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false); // ����
			// ����
			if (!isMyLocationEnabled) { // ��ġ Ž���� �Ұ����ϸ�
				Toast.makeText(MyMap.this, "GPS ���񽺸� ������ �ּ���",
						Toast.LENGTH_LONG).show();
				Intent goToSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(goToSettings);
				return;
			}
		}
	}

	// �� ��ġ ã�� ���� �Լ� ����
	private void stopMyLocation() {
		mMapLocationManager.disableMyLocation(); // ���� ��ġ Ž�� ����
		if (mMapView.isAutoRotateEnabled()) { // ���� ȸ������� Ȱ��ȭ ���¶��
			mMyLocationOverlay.setCompassHeadingVisible(false); // ��ħ�� ����ǥ�� ����
			mMapCompassManager.disableCompass(); // ��ħ�� ����͸� ����
			mMapView.setAutoRotateEnabled(false, false); // ���� ȸ����� ����
			mMapContainerView.requestLayout();
		}
	}

	protected Dialog onCreateDialog(int id, Bundle args) {
		if(id==DIALOG_MODE){
			AlertDialog.Builder dialog1 = new Builder(this);
			final String str1[] = { "��������", "��������" };
			dialog1.setTitle("�ɼ� ����");
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
