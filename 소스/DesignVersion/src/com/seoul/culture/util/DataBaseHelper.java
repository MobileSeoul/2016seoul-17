package com.seoul.culture.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "list.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DB_PATH_SUFFIX = "/databases/";
	Context mCotext;

	public DataBaseHelper(Context context)  {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mCotext = context;
	}

	public ArrayList<Seoul> Get_SeoulDetails() {
		ArrayList<Seoul> myseoul = new ArrayList();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("center", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			myseoul.add(new Seoul(cursor.getInt(0),cursor.getString(1), cursor.getString(2), 
					cursor.getString(3),cursor.getString(4), cursor.getString(5) ,cursor.getString(6)));
		}
		cursor.close();
		db.close();
		return myseoul;
	}

	public Seoul Search_CenterDetails(String name) {
		SQLiteDatabase db = getReadableDatabase();
		Seoul myseoul = new Seoul();

		Cursor cursor = db.query("center", null, "name=?", new String[] { name }, null, null, null);
		while (cursor.moveToNext()) {
			myseoul = new Seoul(cursor.getInt(0),cursor.getString(1), cursor.getString(2), 
					cursor.getString(3),cursor.getString(4), cursor.getString(5) ,cursor.getString(6));
		}
		cursor.close();
		db.close();
		return myseoul;
	}

	public void CopyDataBaseFromAsset() throws IOException{

		InputStream myInput = mCotext.getAssets().open(DATABASE_NAME);
		File f = new File(mCotext.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
		if (!f.exists()) {
			f.mkdir();
		}

		String outFileName = mCotext.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public SQLiteDatabase openDataBase() throws SQLException{
		File dbFile = mCotext.getDatabasePath(DATABASE_NAME);

		if (!dbFile.exists()) {
			try {
				CopyDataBaseFromAsset();
				System.out.println("Copying sucess from Assets folder");
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

