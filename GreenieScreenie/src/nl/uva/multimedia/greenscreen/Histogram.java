package nl.uva.multimedia.greenscreen;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public abstract class Histogram {
	public Point position;
	protected Point size = new Point();
	public PointF range;
	
	public Histogram(Point pos, Point size, PointF range) {
		this.position = pos;
		this.size = size;
		this.pixelBins = new int[size.x * size.y];
		this.range = range;
	}
	
	private int[] pixelBins;
	
	public void setSize(int x, int y) {
		size.x = x;
		size.y = y;
		pixelBins = new int[size.x * size.y];
	}
	
	private Paint bg = new Paint(); {
		bg.setColor(0xFF555555);
	}
	private Paint front = new Paint(); {
		front.setColor(Color.WHITE);
		front.setTextSize(15);
	}
	
	protected abstract void drawPixel(Canvas canvas, int x, int y, double intensity);
	protected abstract String getXAxis();
	protected abstract String getYAxis();
	
	private DecimalFormat rangeFormat = new DecimalFormat("0.##");
	
	private void drawBase(Canvas canvas) {
		canvas.drawRect(0, 0, size.x, size.y, bg);
		canvas.drawRect(0, size.y - 1, size.x, size.y, front);
		canvas.drawRect(0, 0, 1, size.y, front);
		
		canvas.drawText(getXAxis(), size.x / 2 - 8, size.y + 20, front);
		canvas.drawText(getYAxis(), -20, size.y / 2 + 8, front);
		
		canvas.drawText("0", -17, size.y + 17, front);
		canvas.drawText(rangeFormat.format(range.x), size.x - 8, size.y + 17, front);
		canvas.drawText(rangeFormat.format(range.y), -17, 8, front);
	}
	
	public void draw(Canvas canvas, float[] data) {
		if (data.length == 0)
			return;

		for (int i = 0; i < pixelBins.length; i++) {
			pixelBins[i] = 0;
		}
		
		int maxAmount = 1;
		//int maxIndex = 0, oneCount = 0;
		for (int i = 0; i < data.length - 1; i += 2) {
			int x = (int)((size.x - 1) * data[i] / range.x);
			int y = (int)((size.y - 1) * data[i + 1] / range.y);
			int index = x + y * size.x;

			//pixelBins[index] += 1;
			int amount = ++pixelBins[index];
			/* if (index == 76560) {
				Log.i("draw", "data[i]: " + data[i] + " data[i+1]: " + data[i+1] + " index: " + index);
				Log.i("draw", "pixelBins[" + index + "] is now equal to" + pixelBins[index]);
			} */
			if (amount > maxAmount) {
				maxAmount = amount;
				//maxIndex = index;
			}
		}
		
		//Log.i("draw", "maxAmount: " + maxAmount + ", maxIndex: " + maxIndex);
		
		canvas.save();
		canvas.translate(position.x, position.y);
		
		drawBase(canvas);
		
		double logMaxAmount = Math.log(maxAmount);
		
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				int amount = pixelBins[x + y * size.x];
				double intensity = Math.log(amount) / logMaxAmount;
				if (amount > 0) {
					drawPixel(canvas, x, size.y - y, intensity);
				}
			}
		}

		canvas.restore();
	}
	
	/*
	 * Exactly the same method as above, but for int[] instead of float[] data.
	 * Not really a better way to do this in Java...
	 */
	public void draw(Canvas canvas, int[] data) {
		if (data.length == 0)
			return;

		for (int i = 0; i < pixelBins.length; i++) {
			pixelBins[i] = 0;
		}
		
		int maxAmount = 1;
		for (int i = 0; i < data.length - 1; i += 2) {
			int x = (int)((size.x - 1) * data[i] / range.x);
			int y = (int)((size.y - 1) * data[i + 1] / range.y);
			int index = x + y * size.x;

			int amount = ++pixelBins[index];
			if (amount > maxAmount) {
				maxAmount = amount;
			}
		}
		
		canvas.save();
		canvas.translate(position.x, position.y);
		
		drawBase(canvas);
		
		double logMaxAmount = Math.log(maxAmount);
		
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				int amount = pixelBins[x + y * size.x];
				double intensity = Math.log(amount) / logMaxAmount;
				if (amount > 0) {
					drawPixel(canvas, x, size.y - y, intensity);
				}
			}
		}

		canvas.restore();
	}
}
