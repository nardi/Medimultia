package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class HSVHistogram extends Histogram {
	public HSVHistogram(Point pos, Point size) {
		super(pos, size, new PointF(360, 1));
	}
	
	private float[] hsv = new float[3];
	private Paint paint = new Paint();

	/*
	 * Data points with x- and y-values interleaved
	 * 
	 * TODO Deze methode (en klasse) kan gedeeltelijk naar Histogram verplaatst worden denk ik
	 */
	protected void drawPixel(Canvas canvas, int x, int y, int amount, int maxAmount) {
		hsv[0] = x * size.x / range.x;
		hsv[1] = y * size.y / range.y;
		hsv[2] = (float)amount / maxAmount;

		if (amount == 0)
			paint.setColor(0);
		else
			paint.setColor(Color.HSVToColor(hsv));
		canvas.drawPoint(x, y, paint);
	}
}
