package com.example.touchpoint;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteDAO {

	private SQLiteDatabase db;
	private MysqliteHelper helper;
	
	public SqliteDAO(Context context) {
		helper = new MysqliteHelper(context);
	}
	
	public void open() {
		db = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}
	public void writePositionToDB(double latitude,double longitude) {
		ContentValues values = new ContentValues();
		values.put(MysqliteHelper.COLUMN_LATITUDE, latitude);
		values.put(MysqliteHelper.COLUMN_LONGITUDE, longitude);
		db.insert("routlist", null, values);
	}

	public List<LocationPoint> getAllPoints() {
		List<LocationPoint> points = new ArrayList<LocationPoint>();

		Cursor cursor = db.query("routlist", MysqliteHelper.allColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			LocationPoint point = cursorToPoint(cursor);
			points.add(point);
			cursor.moveToNext();
		}
		// 记得关闭cursor对象
		cursor.close();
		return points;
	}
	
	private LocationPoint cursorToPoint(Cursor cursor) {
		LocationPoint point = new LocationPoint();
		point.set_id(cursor.getLong(0));
		point.setLatitude(cursor.getDouble(1));
		point.setLongitutde(cursor.getDouble(2));
		Log.d("route",point.toString());
		return point;
	}
}
