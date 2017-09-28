package com.seoul.culture;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.seoul.culture.util.MyPagerAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class MainActivity  extends AppCompatActivity {
	private final int DIALOG_MODE = 0;
	private FragmentTabHost mTabHost;
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	private SharedPreferences sp;
	private MenuItem searchMenuItem;
	private SearchView searchView;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private TextView tv[] = new TextView[4];
	public String test = "";
	private InputMethodManager imm;
	boolean keyboard = false;
	public String array;
	private CoordinatorLayout cl;
	private long backKeyPressedTime = 0;
	String savedName = "";
	private final int SPLASH_DISPLAY_LENGTH = 1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setHomeButtonEnabled(true);

		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		pager = (ViewPager) findViewById(R.id.viewpager);                                   
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, FragCategory.class.getName()));
		fragments.add(Fragment.instantiate(this, FragChoice.class.getName()));
		fragments.add(Fragment.instantiate(this, FragMap.class.getName()));         
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				mTabHost.setCurrentTab(arg0);
				if(keyboard){
					imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
				}
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				pager.setCurrentItem(mTabHost.getCurrentTab());
			}
		});

		mTabHost.addTab(
				mTabHost.newTabSpec("tab1").setIndicator("강좌", null),
				FragCategory.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec("tab2").setIndicator("센터", null),
				FragChoice.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec("tab3").setIndicator("지도", null),
				FragMap.class, null);

		dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name){
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		dlDrawer.setDrawerListener(dtToggle);
		Intent intent = getIntent();
		boolean draw = intent.getBooleanExtra("draw", false);
		if(draw){
			dlDrawer.openDrawer(Gravity.START);
		}

		sp = getSharedPreferences("bookmark", MODE_PRIVATE);
		cl = (CoordinatorLayout)findViewById(R.id.cl);
		cl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("seoul", "클릭");
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
			}
		});

		tv[0] = (TextView) findViewById(R.id.txtOne);
		tv[1] = (TextView) findViewById(R.id.txtTwo);
		tv[2] = (TextView) findViewById(R.id.txtThree);
		tv[3] = (TextView) findViewById(R.id.txtFour);
		for (int i = 0; i < tv.length; i++) {
			tv[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TextView tv = (TextView) v;
					if(!tv.getText().toString().contains("즐겨찾기")){
						savedName = tv.getText().toString();
						showDialog(DIALOG_MODE);
					} else {
						Snackbar.make(cl, "즐겨찾기 내역을 등록한 후에 이용해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					}
				}
			});
		}

		ImageView iv = (ImageView)findViewById(R.id.imageView1);
		iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});

		TextView Bookmark = (TextView) findViewById(R.id.textView1);
		Bookmark.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});

		TextView appinfo = (TextView) findViewById(R.id.textView6);
		appinfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intentAppInfo = new Intent(getApplicationContext(), ShowActivity.class);
				startActivity(intentAppInfo);
			}
		});

	}//end of onCreate


	@Override
	public void onBackPressed() {
		Snackbar sb = Snackbar.make(cl, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_LONG).setAction("Action", null);
		if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
			backKeyPressedTime = System.currentTimeMillis();
			sb.show();
			return;
		}
		if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		String add = sp.getString("add", "");
		Log.d("seoul", "저장내역 = "+add);
		ArrayList<String> now = new ArrayList<String>();
		for (int i = 0; i < add.split("#").length; i++) {
			if(!TextUtils.isEmpty(add.split("#")[i])){
				now.add(add.split("#")[i]);
				Log.d("seoul", i+" : "+add.split("#")[i]);
			}
		}
		for (int i = 0; i < tv.length; i++) {
			tv[i].setText("즐겨찾기"+(i+1));
		}
		for (int i = 0; i < now.size(); i++) {
			tv[i].setText(now.get(i));
		}
	}

	@Override // 메뉴버튼과 검색버튼 적용
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu, menu);

		MenuItem home = menu.findItem(R.id.home);


		searchMenuItem = menu.findItem(R.id.search_btn); // get my MenuItem with placeholder submenu
		searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		searchView.setQueryHint("센터나 강좌명을 입력해주세요");
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String s) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		// SearchView 검색어 입력/검색 이벤트 처리
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setQuery("", false);
				searchView.onActionViewCollapsed();
				Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
				intent.putExtra("keyword", query);
				startActivity(intent);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}
		});


		return true;
	}



	// 메뉴버튼과 검색버튼 동작하는 구문
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.d("seoul", "click");
			if (dlDrawer.isDrawerOpen(Gravity.START)) {
				dlDrawer.closeDrawer(Gravity.START);
			} else {
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
				dlDrawer.openDrawer(Gravity.START);
			}
			break;
		case R.id.search_btn:
			searchMenuItem.expandActionView(); 
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		dtToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dtToggle.onConfigurationChanged(newConfig);
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
					intent.putExtra("name", savedName);
					startActivity(intent);
				}
			});
			return dialog1.create();
		}
		return super.onCreateDialog(id, args);
	}


}