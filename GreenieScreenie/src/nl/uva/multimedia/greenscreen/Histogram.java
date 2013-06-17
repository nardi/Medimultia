package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class Histogram {
	public Point position;
	protected Point size;
	public PointF range;
	
	public Histogram(Point pos, Point size, PointF range) {
		this.position = pos;
		setSize(size);
		this.range = range;
	}
	
	public void setSize(Point size) {
		this.size = size;
	}	
}
