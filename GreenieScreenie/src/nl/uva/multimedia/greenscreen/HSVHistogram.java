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

	private int[] pixelBins;
	private float[] hsv = new float[3];
	
	public void setSize(Point size) {
		super.setSize(size);
		pixelBins = new int[size.x * size.y];
	}
	
	private Paint paint = new Paint();

	/*
	 * Data points with x- and y-values interleaved
	 * 
	 * TODO Deze methode (en klasse) kan gedeeltelijk naar Histogram verplaatst worden denk ik
	 */
	public void draw(Canvas canvas, float[] data) {
		if (data.length == 0)
			return;

		for (int i = 0; i < pixelBins.length; i++) {
			pixelBins[i] = 0;
		}
		
		int maxAmount = 1;
		for (int i = 0; i < data.length; i += 2) {
			int x = (int)(size.x * data[i] / range.x);
			int y = (int)(size.y * data[i + 1] / range.y);
			
			int amount = ++pixelBins[x + y * size.x];
			if (amount > maxAmount) {
				maxAmount = amount;
			}
		}
		
		canvas.save();
		canvas.translate(position.x, position.y);
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				hsv[0] = x * size.x / range.x;
				hsv[1] = y * size.y / range.y;
				hsv[2] = (float)pixelBins[x + y * size.x] / maxAmount;

				paint.setColor(Color.HSVToColor(hsv));
				canvas.drawPoint(x, y, paint);
			}
		}

		canvas.restore();
	}
}
