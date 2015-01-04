package com.example.touchpoint;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;

public class PointOverlay extends ItemizedOverlay<OverlayItem> {

	List<OverlayItem> items = new ArrayList<OverlayItem>();

	public PointOverlay(Drawable arg0) {
		super(arg0);
	}

	@Override
	protected OverlayItem createItem(int arg0) {

		return items.get(arg0);
	}

	@Override
	public int size() {
		return items.size();
	}

	public void addItem(OverlayItem item) {
		items.add(item);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		Projection proj = mapView.getProjection();
		
		Path mPath = new Path();
		for(OverlayItem item : items) {
			GeoPoint gp = item.getPoint();
			Point mPoint = proj.toPixels(gp, null);

			mPath.lineTo(mPoint.x, mPoint.y);
		}
		canvas.drawPath(mPath, null);
		
		super.draw(canvas, mapView, shadow);
	}

}
