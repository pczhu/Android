package com.example.touchpoint;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.location.LocationProviderProxy;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;

public class MainActivity extends MapActivity implements LocationListener {
	private MapView mMapVIew2;
	private MapView mMapView;

	private MapController controller;

	private List<Overlay> mapOverlayList;

	private LocationManagerProxy locationProxy;

	private SqliteDAO dao;

	private RouteItemizedOverlay routeOverlay;
	
	private MyOverlay overlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMapView = (MapView) findViewById(R.id.main_mapView);
		mMapView.setBuiltInZoomControls(true); // 开启缩放控件

		mMapView.setSatellite(true); // 设置为卫星模式

		controller = mMapView.getController();

		controller.setZoom(3);

		mapOverlayList = mMapView.getOverlays();

		locationProxy = LocationManagerProxy.getInstance(this);

		Drawable d = getResources().getDrawable(R.drawable.new_func_dot);

		routeOverlay = new RouteItemizedOverlay(d);

		dao = new SqliteDAO(this);

	}

	@Override
	protected void onPause() {
		locationProxy.removeUpdates(this);
		dao.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		locationProxy.requestLocationUpdates(
				LocationProviderProxy.MapABCNetwork, 0, 0, this);
		dao.open();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.showroute:
			showRoutlist();
			break;
		case R.id.closeroute:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showRoutlist() {
		mapOverlayList.remove(overlay);
		locationProxy.removeUpdates(this); // 取消位置监听（数据库不会有更新)

		routeOverlay.addMyRoute(dao.getAllPoints());

		mapOverlayList.add(routeOverlay);
	}

	public void closeRoute() {
		mapOverlayList.clear(); // 清空路线
		Location last = locationProxy
				.getLastKnownLocation(LocationProviderProxy.MapABCNetwork);

		mapOverlayList.add(new MyOverlay(this, new GeoPoint((int) (last
				.getLatitude() * 1E6), (int) (last.getLongitude() * 1E6))));
		locationProxy.requestLocationUpdates(LocationManagerProxy.GPS_PROVIDER,
				0, 0, this); // 开始监听位置变化
	}

	public void onLocationChanged(Location location) {
		mapOverlayList.clear();
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

			dao.writePositionToDB(latitude, longitude); // 写进数据库

			GeoPoint gp = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
		    overlay = new MyOverlay(this, gp);
			mapOverlayList.add(overlay);
		}
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public void onProviderEnabled(String provider) {

	}

	public void onProviderDisabled(String provider) {

	}

	public MapView getmMapVIew2() {
		System.out.println("team commit");
		return mMapView;
	}

	public void setmMapVIew2(MapView mMapVIew2) {
		this.mMapVIew2 = mMapVIew2;
	}

}

class MyOverlay extends Overlay {

	private Context mContext;
	private GeoPoint gp;
	private Bitmap bitmap;

	public MyOverlay(Context mContext, GeoPoint gp) {
		this.mContext = mContext;
		this.gp = gp;
		bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.new_func_dot);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection proj = mapView.getProjection();
		Point mPoint = proj.toPixels(gp, null);
		canvas.drawBitmap(bitmap, mPoint.x, mPoint.y, null);
		super.draw(canvas, mapView, shadow);
	}

}
