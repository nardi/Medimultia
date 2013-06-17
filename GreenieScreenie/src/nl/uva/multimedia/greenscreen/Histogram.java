package nl.uva.multimedia.greenscreen;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class Histogram {
	public Point position, size;
	public PointF range;
	
	public Histogram(Point pos, Point size, PointF range) {
		this.position = pos;
		this.size = size;
		this.range = range;
	}

	private RectF drawRect = new RectF();
	private PointF point = new PointF();
	
	/*
	 * Data points with x- and y-values interleaved
	 */
	public void draw(Canvas canvas, int[] data) {
		if (data.length == 0)
			return;
		
		point.x = size.x / range.x;
		point.y = size.y / range.y;
		
		canvas.translate(position.x, position.y);
		
		for (int i = 0; i < data.length; i += 2) {
			int x = data[i], y = data[i + 1];
			
			drawRect.left = (x / range.x) * size.x;
			drawRect.top = (y / range.y) * size.y;
			drawRect.right = drawRect.left + point.x;
			drawRect.bottom = drawRect.top + point.y;

			canvas.drawRect(drawRect, null);
		}
	}
}
