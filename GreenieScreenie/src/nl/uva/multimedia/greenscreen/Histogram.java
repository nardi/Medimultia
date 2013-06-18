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
		bg.setColor(0xFF333333);
	}
	private Paint front = new Paint(); {
		front.setColor(Color.WHITE);
		front.setTextSize(15);
	}
	
	protected abstract void drawPixel(Canvas canvas, int x, int y, float intensity);
	protected abstract String getXAxis();
	protected abstract String getYAxis();
	
	private DecimalFormat rangeFormat = new DecimalFormat("0.##");
	
	private void emptyPixelBins() {
		for (int i = 0; i < pixelBins.length; i++) {
			pixelBins[i] = 0;
		}
	}
	
	private void drawBase(Canvas canvas, int maxAmount) {
		canvas.save();
		canvas.translate(position.x, position.y);
		
		canvas.drawRect(0, 0, size.x, size.y, bg);
		canvas.drawRect(0, 0, size.x, 1, front);
		canvas.drawRect(0, 0, 1, size.y, front);
		
		canvas.drawText(getXAxis(), size.x / 2 - 8, -10, front);
		canvas.drawText(getYAxis(), -15, size.y / 2 + 8, front);
		
		canvas.drawText("0", -10, -10, front);
		canvas.drawText(rangeFormat.format(range.x), size.x - 8, -10, front);
		canvas.drawText(rangeFormat.format(range.y), -10, size.y + 8, front);
		
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				int amount = pixelBins[x + y * size.x];
				float intensity = (float)(Math.sqrt(amount) / Math.sqrt(maxAmount));
				if (amount > 0) {
					drawPixel(canvas, x, y, intensity);
				}
			}
		}

		canvas.restore();
	}
	
	public void draw(Canvas canvas, float[] data) {
		if (data.length == 0)
			return;

		emptyPixelBins();
		
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
		
		drawBase(canvas, maxAmount);
	}
	
	/*
	 * Exactly the same method as above, but for int[] instead of float[] data.
	 * Not really a better way to do this in Java...
	 */
	public void draw(Canvas canvas, int[] data) {
		if (data.length == 0)
			return;

		emptyPixelBins();
		
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
		
		drawBase(canvas, maxAmount);
	}
}
