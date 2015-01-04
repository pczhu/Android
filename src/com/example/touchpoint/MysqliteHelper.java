package com.example.touchpoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MysqliteHelper extends SQLiteOpenHelper{


	private static final String DATABASE_NAME = "my.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String COLUMN_ID = "_id";	//必须的
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_UPLOAD_TIME = "upload_time";	//暂时没用
	
	
	public static final String[] allColumns = {COLUMN_ID,COLUMN_LATITUDE,COLUMN_LONGITUDE};
	
	private static final String CREATE_TABLE = "create table if not exists routlist"
											 + "("
											 + COLUMN_ID + " integer primary key autoincrement, "
											 + COLUMN_LATITUDE + " REAL not null, "
											 + COLUMN_LONGITUDE + " REAL not null "
											 + ");";
	
	public MysqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("CREATE_TABLE",CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table " + CREATE_TABLE);
		onCreate(db);
	}
}
