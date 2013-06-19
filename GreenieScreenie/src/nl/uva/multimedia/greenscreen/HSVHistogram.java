/*
 * Histogram for the HSV colour space
 */

package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

public class HSVHistogram extends Histogram {
	public HSVHistogram(Point pos, Point size) {
		super(pos, size, new PointF(360, 1));
	}
	
	private float[] hsv = new float[3];

	protected void drawPixel(Canvas canvas, int x, int y, float intensity) {
		hsv[0] = range.x * x / (float)size.x;
		hsv[1] = range.y * y / (float)size.y;
		hsv[2] = 0.5f + 0.5f * intensity;

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
