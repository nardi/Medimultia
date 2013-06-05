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
		this.bins = new int[numBins];
	}

	public void draw(Canvas canvas, int[] sortedData) {
		for (int i = 0; i < bins.length; i++)
			bins[i] = 0;
		
		float binSize = Math.round((float)range / bins.length);
		for (int i = 0; i < sortedData.length; i++) {
			int index = Math.min((int)(sortedData[i] / binSize), bins.length);
			this.bins[index]++;
		}

		int binWidth = size.x / bins.length;
		canvas.save();
		canvas.translate(pos.x, pos.y);

		canvas.drawRect(new Rect(0, 0, size.x, size.y), bg);
		
		for (int i = 0; i < bins.length; i++) {
			int binHeight = size.y * bins[i] / sortedData.length;
			canvas.drawRect(new Rect(0, size.y - binHeight, binWidth, size.y), paint);
			canvas.translate(binWidth, 0);
		}
		canvas.restore();
	}
}
