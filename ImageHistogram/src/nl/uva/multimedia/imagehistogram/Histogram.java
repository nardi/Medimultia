package nl.uva.multimedia.imagehistogram;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Histogram {
	public Point pos, size;
	public int range;
	private int[] bins;
	private int binSize;
	private Paint paint, bg, text;
	
	public Histogram(Point pos, Point size, int range, int binSize) {
		this.pos = pos;
		this.size = size;
		this.range = range;
		this.setBinSize(binSize);

		this.paint = new Paint();
		paint.setColor(Color.rgb(42, 255, 42));
		this.bg = new Paint();
		bg.setColor(Color.DKGRAY);
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
		/* Log.v("Histogram", "binSize: " + binSize + ", numBins: " + bins.length);
		for (int i = 0; i <= range; i++) {
			Log.v("Histogram", "Value: " + i + ", Bin: " + this.findBin(i));
		} */
	}

	public int getBinSize() {
		return binSize;
	}
	
	public int getNumBins() {
		return bins.length;
	}
	
	private int findBin(int sample) {
		return Math.min(sample / this.getBinSize(), bins.length - 1);
	}

	public void draw(Canvas canvas, int[] data, boolean absolute, boolean labels) {
		for (int i = 0; i < bins.length; i++)
			bins[i] = 0;

		for (int i = 0; i < data.length; i++) {
			int index = this.findBin(data[i]);
			if (index >= bins.length)
				Log.e("Histogram", "Value: " + data[i] + ", Bin " + index + " of " + bins.length);
			this.bins[index]++;
		}

		int space = 1;
		int binWidth = (size.x - (bins.length - 1) * space) / bins.length;
		if (binWidth == 0) {
			space = 0;
			binWidth = size.x / bins.length;
		}
		int binSize = this.getBinSize();
		canvas.save();
		canvas.translate(pos.x, pos.y);

		canvas.drawRect(new Rect(0, 0, size.x, size.y), bg);

		int maxHeight = 0;
		if (absolute)
			maxHeight = data.length;
		else {
			for (int i = 0; i < bins.length; i++)
				maxHeight = Math.max(maxHeight, bins[i]);
		}

		if (maxHeight != 0) {
			for (int i = 0; i < bins.length; i++) {
				int binHeight = (int)(size.y * bins[i] / maxHeight);
				if (labels)
					canvas.drawText(Integer.toString(i * binSize) + " - " + Integer.toString((i + 1) * binSize), 0, size.y - binHeight - 1, text);
				canvas.drawRect(new Rect(0, size.y - binHeight, binWidth, size.y), paint);
				canvas.translate(binWidth + space, 0);
			}
		}
		canvas.restore();
	}
}
