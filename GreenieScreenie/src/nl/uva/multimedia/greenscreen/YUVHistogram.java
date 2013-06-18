package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

public class YUVHistogram extends Histogram {
	
	
	public YUVHistogram(Point pos, Point size) {
		super(pos, size, new PointF(100, 100));
	}

	@Override
	protected void drawPixel(Canvas canvas, int x, int y, float intensity) {
		int Y = Math.round(intensity);
		int U = Math.round(range.x * x / (float)size.x);
		int V = Math.round(range.y * y / (float)size.y);
		
		paint.setColor(Color.RED);
		canvas.drawPoint(x, y, paint);
	}

	@Override
	protected String getXAxis() {
		return "U";
	}

	@Override
	protected String getYAxis() {
		return "V";
	}

}
