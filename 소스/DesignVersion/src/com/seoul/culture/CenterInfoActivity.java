package com.seoul.culture;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.seoul.culture.map.MiniMap;
import com.seoul.culture.util.CSVFile;
import com.seoul.culture.util.Center;
import com.seoul.culture.util.DataBaseHelper;
import com.seoul.culture.util.GuCenter;
import com.seoul.culture.util.Info;
import com.seoul.culture.util.MyList;
import com.seoul.culture.util.Seoul;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CenterInfoActivity extends AppCompatActivity{
	String[] arrData;
	private DataBaseHelper dbHelper;
	private String name;
	int[] select = new int[2];
	ArrayList<String> menuitem = new ArrayList<String>();
	private ArrayList<Seoul> myseoul = new ArrayList<Seoul>();
	private ArrayList<String> mygu = new ArrayList<String>();
	private ArrayList<String> mycenter = new ArrayList<String>();
	private ArrayList<GuCenter> arrGC;
	private ActionBar ab;
	private MenuItem staritem;
	private SharedPreferences sp;
	private CoordinatorLayout cl;
	final int DIALOG_CHANGE = 0;
	private Seoul center;
	private ArrayList<Center> all;
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_info);


		// �ʿ��� ����Ʈ ��(ȭ�� ����)
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		if (TextUtils.isEmpty(name)) {
			name = "���򱸹�ü������";
		}

		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setHomeButtonEnabled(true);
		ab.setTitle(name);
		ab.setDisplayUseLogoEnabled(true);

		cl = (CoordinatorLayout)findViewById(R.id.cl);
		sp = getSharedPreferences("bookmark", MODE_PRIVATE);

		// ��ü ���� DB �ҷ�����
		dbHelper = new DataBaseHelper(this);
		dbHelper.openDataBase();
		myseoul = dbHelper.Get_SeoulDetails();

		for (int i = 0; i < myseoul.size(); i++) {
			if (myseoul.get(i).name.equals(name)) {
				((MiniMap)MiniMap.mContext).testOverlayMaker(myseoul.get(i).nmap, name);
			}
		} 

		// ���ǳ� �����͸� ���� ��/���� ����
		GuCenter gc = new GuCenter();
		arrGC = gc.update(myseoul);
		for (int i = 0; i < arrGC.size(); i++) {
			mygu.add(arrGC.get(i).getGu());
			for (int j = 0; j < arrGC.get(i).name.size(); j++) {
				if(ab.getTitle().equals(arrGC.get(i).name.get(j))){
					mycenter.add(arrGC.get(i).name.get(j));
				}
			}
		}

		try {
			InputStream inputStream = this.getAssets().open("seouldata.csv");
			CSVFile csvFile = new CSVFile(inputStream);
			all = new ArrayList<Center>();
			all = csvFile.read();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		lv = (ListView)findViewById(R.id.listView1);
		reset(name);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final int temp = position;
				
				Snackbar.make(cl, "�ڼ��� ������ Ȯ���غ��ðڽ��ϱ�?",
						Snackbar.LENGTH_LONG)
				.setAction("Ȯ��", new OnClickListener() {
					public void onClick(View v) {
						myIntent(temp);
					}
				}).show();
			}
		});


	}//end of onCreate
	
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MENU:
                	Log.d("seoul", "click");
                    return true;
            }

        }
        return super.dispatchKeyEvent(event);
    }
	
	private void myIntent(int position) {
		Intent intent = null;
		switch (position) {
		case 0: // �ּ�
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+ab.getTitle()));
			startActivity(intent);
			break;
		case 1: // ��ȭ
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+center.number));
			startActivity(intent);
			break;
		case 2: // Ȩ������
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(center.page));
			startActivity(intent);
			break;
		case 3: case 4: case 5: case 6: // ���¼Ұ�
			intent = new Intent(getApplicationContext(), CenterDetailActivity.class);
			intent.putExtra("name", ab.getTitle());
			intent.putExtra("type", position-3);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.info_menu, menu);
		staritem = menu.findItem(R.id.menu_btn1);
		MenuItem title = menu.findItem(R.id.action_bar_title);

		if(sp.getString("add", "").contains(ab.getTitle())){
			staritem.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
		}
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_btn0:
			showDialog(DIALOG_CHANGE);
			break;
		case R.id.menu_btn1:
			String before = sp.getString("add", "");
			ArrayList<String> now = new ArrayList<String>();
			for (int i = 0; i < before.split("#").length; i++) {
				if(!TextUtils.isEmpty(before.split("#")[i])){
					now.add(before.split("#")[i]);
					Log.d("seoul", i+" : "+before.split("#")[i]);
				}
			}
			String after = "";

			// 1. ���� �� ���� Ȯ��
			if(before.contains(ab.getTitle())){
				after = before.replace("#" + ab.getTitle(), "");
				staritem.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
				Log.d("seoul", after + "����");

				Editor editor = sp.edit();
				editor.putString("add", after);
				Log.d("seoul", "���ã�� = "+ after);
				editor.commit();
			} else{
				if(now.size()==4){// 2. ����Ȯ��
					Snackbar.make(cl, "���ã�� ����� ��� ����Ǿ��ֽ��ϴ�.\n���ã�� ���� �� �߰����ּ���.", 					Snackbar.LENGTH_LONG)
					.setAction("���", new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							intent.putExtra("draw", true);
							startActivity(intent);
							finish();
						}
					}).show();
				} else{
					after = before + "#" + ab.getTitle();
					staritem.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
					Log.d("seoul", after + "����");

					// 3. �߰�	
					Editor editor = sp.edit();
					editor.putString("add", after);

					Log.d("seoul", "���ã�� = "+ after);
					editor.commit();
				}
			} 

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==DIALOG_CHANGE){

			int gu = 0, center = 0;
			for (int i = 0; i < arrGC.size(); i++) {
				for (int j = 0; j < arrGC.get(i).name.size(); j++) {
					if(ab.getTitle().equals(arrGC.get(i).name.get(j))){
						gu = i;
						center = j;
					}
				}
			}

			View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_change, null);
			WheelView mainWheelView = (WheelView)outerView.findViewById(R.id.main_wheelview);
			mainWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
			mainWheelView.setSkin(WheelView.Skin.Holo);
			mainWheelView.setWheelData(createMainDatas());
			WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
			style.selectedTextSize = 20;
			style.textSize = 16;
			mainWheelView.setStyle(style);

			final WheelView subWheelView = (WheelView) outerView.findViewById(R.id.sub_wheelview);
			subWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
			subWheelView.setSkin(WheelView.Skin.Holo);
			subWheelView.setWheelData(createSubDatas().get(createMainDatas().get(mainWheelView.getSelection())));
			subWheelView.setStyle(style);
			mainWheelView.join(subWheelView);
			mainWheelView.joinDatas(createSubDatas());

			mainWheelView.setSelection(gu);
			subWheelView.setSelection(center);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("���ϴ� ������ �������ּ���")
			.setView(outerView)
			.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					name = subWheelView.getSelectionItem()+"";
					Log.d("seoul", subWheelView.getSelectionItem()+"");
					for (int i = myseoul.size() - 1; i >= 0; i--) {
						if(name.equals(myseoul.get(i).name)){
							((MiniMap)MiniMap.mContext).testOverlayMaker(myseoul.get(i).nmap, name);
						}
					}
					reset(name);

					String before = sp.getString("add", "");
					if(!before.contains(name)){
						Log.d("seoul", "���ã�� = " + before + "����");
						staritem.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
					} else {
						Log.d("seoul", "���ã�� = " + before + "����");
						staritem.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
					}

				}
			})
			.setNegativeButton("���", null)
			.setCancelable(false);
			return dialog.create();
		}
		return null;
	}




	private List<String> createMainDatas() {
		return mygu;
	}

	private HashMap<String, List<String>> createSubDatas() {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < arrGC.size(); i++) {
			map.put(arrGC.get(i).gu, arrGC.get(i).name);
		}
		return map;
	}

	void reset(String str) {
		ab.setTitle(str);

		ArrayList<Info> arrInfo = new ArrayList<Info>();
		arrInfo.clear();

		center = dbHelper.Search_CenterDetails(name);
		arrInfo.add(new Info("�ּ�", center.address));
		arrInfo.add(new Info("��ȭ��ȣ", center.number));
		arrInfo.add(new Info("Ȩ������", center.page));

		ArrayList<String> health = new ArrayList<String>();
		ArrayList<String> culture = new ArrayList<String>();
		ArrayList<String> youth = new ArrayList<String>();
		ArrayList<String> others = new ArrayList<String>();
		for (int i = 0; i < all.size(); i++) {
			if(all.get(i).center.equals(name)){
				if(all.get(i).cafegory.equals("�")){
					health.add(all.get(i).classname);
				} else if(all.get(i).cafegory.equals("��ȭ")){
					culture.add(all.get(i).classname);
				} else if(all.get(i).cafegory.equals("����,û�ҳ�")){
					youth.add(all.get(i).classname);
				} else if(all.get(i).cafegory.equals("��Ÿ")){
					others.add(all.get(i).classname);
				}
			}
		}


		if(health.size()==0){
			arrInfo.add(new Info("� ����", "�ش� ������ �����ϴ�"));
		} else {
			arrInfo.add(new Info("� ����", health.size()+"��"));
			//			for (int i = 0; i < 5; i++) {
			//				String temp[] = new String[5];
			//				temp[i] = health.get(i).toString();
			//			}
		}
		if(culture.size()==0){
			arrInfo.add(new Info("��ȭ ����", "�ش� ������ �����ϴ�"));
		} else {
			arrInfo.add(new Info("��ȭ ����", culture.size()+"��"));
			//			for (int i = 0; i < 5; i++) {
			//				String temp[] = new String[5];
			//				temp[i] = culture.get(i).toString();
			//			}
		}
		if(youth.size()==0){
			arrInfo.add(new Info("û�ҳ� ����", "�ش� ������ �����ϴ�"));
		} else {
			arrInfo.add(new Info("û�ҳ� ����", youth.size()+"��"));
			//			for (int i = 0; i < 5; i++) {
			//				String temp[] = new String[5];
			//				temp[i] = youth.get(i).toString();
			//			}		
		}
		if(others.size()==0){
			arrInfo.add(new Info("��Ÿ ����", "�ش� ������ �����ϴ�"));
		} else {
			arrInfo.add(new Info("��Ÿ ����", others.size()+"��"));
			//			for (int i = 0; i < 5; i++) {
			//				String temp[] = new String[5];
			//				temp[i] = others.get(i).toString();
			//			}	
		}
		lv.setAdapter(new MyList(getApplicationContext(), R.layout.list_row, arrInfo));
	}



}
