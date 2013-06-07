package nl.uva.multimedia.imagehistogram;

/* 
 * This class draws a histogram on a Canvas using an array of data samples.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Histogram {
	public Point position, size;
	public int range;
	private int[] bins;
	private int binSize;
	private Paint paint, bg, text;
	
	public Histogram(Point pos, Point size, int range, int binSize) {
		this.position = pos;
		this.size = size;
		this.range = range;
		this.setBinSize(binSize);

		this.paint = new Paint();
		paint.setColor(Color.rgb(42, 255, 42));
		this.bg = new Paint();
		bg.setColor(0xFF555555);
		this.text = new Paint();
		text.setColor(Color.rgb(234, 234, 234));
		text.setTextSize(10);
	}
	
	public Histogram(Point pos, Point size, int range) {
		this(pos, size, range, range);
	}
	
	public void setBinSize(int binSize) {
		if (binSize < 1)
			binSize = 1;
		if (binSize > range)
			binSize = range;
		this.binSize = binSize;

		this.bins = new int[range / binSize];
	}

	public int getBinSize() {
		return binSize;
	}
	
	public int getNumBins() {
		return bins.length;
	}
	
	/* Find a suitable bin for a sample */
	private int findBin(int sample) {
		return Math.min(sample / this.getBinSize(), bins.length - 1);
	}

	public void draw(Canvas canvas, int[] data, boolean absolute, boolean labels, boolean connectBars) {
		if (data.length == 0)
			return;
		
		/* Empty bins */
		for (int i = 0; i < bins.length; i++){
			bins[i] = 0;
		}

		/* Put data in bins */
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0 || data[i] > range)
				Log.e("Histogram", "Value " + data[i] + "is out of range (" + range + ").");
			this.bins[this.findBin(data[i])]++;
		}

		/* (Optionally) leave one pixel of space between each bar... */
		int space = connectBars ? 0 : 1;
		int binWidth = (size.x - (bins.length - 1) * space) / bins.length;
		/* ...but only if there's still room left for the bars. */
		if (binWidth == 0) {
			space = 0;
			binWidth = size.x / bins.length;
		}
		int binSize = this.getBinSize();
		
		canvas.save();
		canvas.translate(position.x, position.y);

		/* Determine value corresponding to full height */
		int maxValue = 0;
		if (absolute)
			maxValue = data.length;
		else {
			for (int i = 0; i < bins.length; i++)
				maxValue = Math.max(maxValue, bins[i]);
		}

		/* Draw vertical scale */
		int halfTextSize = (int)(text.getTextSize() / 2);
		canvas.drawText("0%", -26, size.y, text);
		canvas.drawText(Integer.toString((100 * maxValue) / data.length) + "%", -26, halfTextSize, text);
		
		int maxHeight = size.y - halfTextSize;
		
		canvas.drawRect(new Rect(0, 0, size.x, maxHeight), bg);
		
		/* Draw bin bars */
		for (int i = 0; i < bins.length; i++) {
			int binHeight = (int)(maxHeight * bins[i] / maxValue);
			if (labels) {
				String label = Integer.toString(i * binSize) + " - " + Integer.toString((i + 1) * binSize);
				canvas.drawText(label, 0, maxHeight - binHeight - 1, text);
			}
			paint.setColor(Color.rgb(0, (i * binSize + (i + 1) * binSize) / 2, 0));
			canvas.drawRect(new Rect(0, maxHeight - binHeight, binWidth, maxHeight), paint);
			canvas.translate(binWidth + space, 0);
		}
		
		canvas.restore();
	}
}
