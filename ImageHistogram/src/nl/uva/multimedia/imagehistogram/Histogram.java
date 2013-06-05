package nl.uva.multimedia.imagehistogram;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Histogram {
	public Point pos, size;
	public int range;
	private int[] bins;
	private Paint paint, bg;
	
	public Histogram(Point pos, Point size, int range, int numBins) {
		this.pos = pos;
		this.size = size;
		this.range = range;
		this.setNumBins(numBins);

		this.paint = new Paint();
		paint.setColor(Color.GREEN);
		this.bg = new Paint();
		bg.setColor(Color.DKGRAY);
	}
	
	public void setNumBins(int numBins) {
		if (numBins < 1 || numBins > range)
			return;
		this.bins = new int[numBins];
	}

	public void draw(Canvas canvas, int[] data, boolean absolute) {
		for (int i = 0; i < bins.length; i++)
			bins[i] = 0;
		
		int binSize = (int)Math.round((float)range / bins.length);
		for (int i = 0; i < data.length; i++) {
			int index = Math.min(data[i] / binSize, bins.length - 1);
			this.bins[index]++;
		}

		int binWidth = size.x / bins.length;
		canvas.save();
		canvas.translate(pos.x, pos.y);

		canvas.drawRect(new Rect(0, 0, size.x, size.y), bg);

		int maxHeight = 0;
		if (absolute)
			maxHeight = size.y;
		else {
			for (int i = 0; i < bins.length; i++)
				maxHeight = Math.max(maxHeight, bins[i]);
		}

		if (maxHeight != 0) {
			for (int i = 0; i < bins.length; i++) {
				int binHeight = size.y * bins[i] / maxHeight;
				canvas.drawRect(new Rect(0, size.y - binHeight, binWidth, size.y), paint);
				canvas.translate(binWidth, 0);
			}
		}
		canvas.restore();
	}
}
