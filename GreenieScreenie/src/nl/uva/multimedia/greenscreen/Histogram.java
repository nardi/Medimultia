package nl.uva.multimedia.greenscreen;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public abstract class Histogram {
	public Point position;
	protected Point size;
	public PointF range;
	
	public Histogram(Point pos, Point size, PointF range) {
		this.position = pos;
		setSize(size);
		this.range = range;
	}
	
	private int[] pixelBins;
	
	public void setSize(Point size) {
		this.size = size;
		pixelBins = new int[size.x * size.y];
	}
	
	public void setSize(int x, int y) {
		size.x = x;
		size.y = y;
		pixelBins = new int[size.x * size.y];
	}
	
	private Paint bg = new Paint(); {
		bg.setColor(0xFF555555);
	}
	
	protected abstract void drawPixel(Canvas canvas, int x, int y, int amount, int maxAmount);
	
	public void draw(Canvas canvas, float[] data) {
		if (data.length == 0)
			return;

		for (int i = 0; i < pixelBins.length; i++) {
			pixelBins[i] = 0;
		}
		
		int maxAmount = 1;
		for (int i = 0; i < data.length - 1; i += 2) {
			int x = (int)(size.x * data[i] / range.x);
			int y = (int)(size.y * data[i + 1] / range.y);
			
			Log.i("draw", "data[i]: " + data[i] + " data[i+1]: " + data[i+1] + " index: " + (x + y * size.x));
			int amount = ++pixelBins[x + y * size.x];
			if (amount > maxAmount) {
				maxAmount = amount;
			}
		}
		
		canvas.save();
		canvas.translate(position.x, position.y);
		
		canvas.drawRect(0, 0, size.x, size.y, bg);
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				drawPixel(canvas, x, y, pixelBins[x + y * size.x], maxAmount);
			}
		}

		canvas.restore();
	}
	
	public void draw(Canvas canvas, int[] data) {
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
		
		canvas.drawRect(0, 0, size.x, size.y, bg);
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				drawPixel(canvas, x, y, pixelBins[x + y * size.x], maxAmount);
			}
		}

		canvas.restore();
	}
}
