package com.delin.dgclient.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import com.delin.dgclient.R;
import com.delin.dgclient.model.Beacon;
import com.delin.dgclient.util.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

	
	
	public static final String DB_PATH = "/data"+Environment.getDataDirectory().getAbsolutePath()+"/com.delin.dgclient/databases/";
	public static final String DB_NAME = "beacons.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "BEACON_LIST";
	private static DBHelper dbHelper;

	private DBHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static DBHelper instance(Context context){
		if (dbHelper==null){
			dbHelper=new DBHelper(context);
		}
		return dbHelper;
	}
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
		db.execSQL(sql);
		db.getVersion();
		onCreate(db);
	}
	
	public ArrayList<Beacon> select(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		ArrayList<Beacon> beacons = new ArrayList();
		if (mCursor.moveToFirst() == true) {
			do {
				Beacon beacon = new Beacon();
				beacon.setBeacon_sn(mCursor.getString(mCursor.getColumnIndex("beacon_sn")));
				beacon.setBeacon_x(mCursor.getDouble(mCursor.getColumnIndex("beacon_x"))+"");
				beacon.setBeacon_y(mCursor.getDouble(mCursor.getColumnIndex("beacon_y"))+"");
				beacon.setBeacon_z(mCursor.getDouble(mCursor.getColumnIndex("beacon_z"))+"");
				beacon.setType(mCursor.getString(mCursor.getColumnIndex("type")));
				beacons.add(beacon);
			}while (mCursor.moveToNext());
		}
		return beacons;
	}
	
	public static boolean isDatabaseExist(){
		SQLiteDatabase checkDB = null;
		String dbFilename = DB_PATH + DB_NAME;
		try{
			checkDB = SQLiteDatabase.openDatabase(dbFilename, null, SQLiteDatabase.OPEN_READONLY);
			if(checkDB != null){
				checkDB.close();
			}
		}catch(SQLiteException e){
			Log.e("database", "failed to open the database;");
			Log.e("error", e.toString());
		}
		return checkDB == null ? false : true;
	}
	
	public void copyDatabaseFile(Context context){
		String dbFilename = DB_PATH + DB_NAME;
		File dir = new File(DB_PATH);
		if(!dir.exists()){
			dir.mkdir();
		}
		FileOutputStream fileOutputStream = null;
		
		try {
			fileOutputStream = new FileOutputStream(dbFilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		InputStream inputStream = context.getResources().openRawResource(R.raw.beacons);
		byte[] buffer = new byte[8192];
		int count = 0;
		try {
			while((count = inputStream.read(buffer)) > 0){
				fileOutputStream.write(buffer, 0, count);
				fileOutputStream.flush();
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			Log.e("DB_VERSION",e.toString());
		}
		Log.d("copy","数据库文件复制完成");
	}




	public void insertData(Beacon beacon){
		final String sql = "INSERT INTO "+TABLE_NAME+"(beacon_sn,beacon_x,beacon_y,beacon_z,type) "+
				"VALUES (?,?,?,?,?)";
		SQLiteDatabase db = getReadableDatabase();
		try{
			db.execSQL(sql,beacon.toStrings());
		}catch (SQLiteConstraintException exception){
			L.d("主键不唯一");
		}

		db.close();
		Log.d("insertData", "数据添加成功");
	}

	public void deleteData(String[] keyValue){
		final String sql = "DELETE FROM "+TABLE_NAME+" WHERE `beacon_sn` = ?";
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL(sql, keyValue);
	}

	public void update(Beacon beacon){

		SQLiteDatabase db = getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("beacon_x",beacon.getBeacon_x());
		contentValues.put("beacon_y",beacon.getBeacon_y());
		contentValues.put("beacon_z",beacon.getBeacon_z());
		contentValues.put("type",beacon.getType());
		db.update(TABLE_NAME,contentValues,"beacon_sn = ?",new String[]{beacon.getBeacon_sn()});

	}
}