package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class HSVHistogram extends Histogram {
	public HSVHistogram(Point pos, Point size) {
		super(pos, size, new PointF(360, 1));
	}
	
	private float[] hsv = new float[3];

	/*
	 * Data points with x- and y-values interleaved
	 * 
	 * TODO Deze methode (en klasse) kan gedeeltelijk naar Histogram verplaatst worden denk ik
	 */

	protected void drawPixel(Canvas canvas, int x, int y, float intensity) {
		hsv[0] = range.x * x / (float)size.x;
		hsv[1] = range.y * y / (float)size.y;
		hsv[2] = 0.4f + 0.6f * intensity;

		paint.setColor(Color.HSVToColor(hsv));
		canvas.drawPoint(x, y, paint);
	}

	String xAxis = "H";
	protected String getXAxis() {
		return xAxis;
	}

	String yAxis = "S";
	protected String getYAxis() {
		return yAxis;
	}
}
