package nl.uva.multimedia.imagehistogram;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX Yes, you should change stuff here */

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.util.AttributeSet;

public class CanvasView extends View implements OnLongClickListener {
	public int[] green;
	public int image_width;
	public int image_height;
	public boolean absolute;
	public boolean labels;
	private boolean connectBars;
	
	Bitmap m_image = null;
	Histogram m_histogram = null;
	Paint paint, text = null;

	{
		m_histogram = new Histogram(new Point(0, 54), new Point(0, 0), 256);
		
		/* Define the basic paint */
		paint = new Paint();
		paint.setColor(Color.rgb(0xa0,0xa0,0xb0));
		paint.setAntiAlias(true);
	
		/* text inherits from the basic paint */
		text = new Paint(paint);
		text.setColor(Color.rgb(234, 234, 234));
		text.setShadowLayer(3.0F,3.0F,3.0F,Color.rgb(0x20,0x20,0x20));
		text.setTextSize(30);
		
		this.setClickable(true);
		this.setOnLongClickListener(this);
	}
	
	public CanvasView(Context context) {
		super(context);
	}
	
	public CanvasView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}
	
	public CanvasView(Context context, AttributeSet attributes, int style) {
		super(context, attributes, style);
	}
	
	@Override protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
	}
	
	/* Called whenever the canvas is dirty and needs redrawing */
	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
				
		canvas.drawColor(Color.DKGRAY);
	
		/* Save state */
		canvas.save();
		canvas.translate(getWidth() * 0.1F, getHeight() * 0.1F);

		if (green != null && image_width > 0 && image_height > 0) {
			Arrays.sort(green);
			
			long mean = 0, stdDev = 0;
			for (int i = 0; i < green.length; i++) {
				mean += green[i];
				stdDev += (long)green[i] * green[i];
			}
			mean /= green.length;
			stdDev = (long)Math.sqrt((stdDev / green.length) - (mean * mean));

			int median = green[green.length / 2];
			if (green.length % 2 == 0) {
		      int prev = green[green.length / 2 - 1];
		      median = (prev + median) / 2;
		    }

			canvas.drawText("Mean: " + mean, 0, 0, text);
			canvas.drawText("Median: " + median, getWidth() * 0.4f, 0, text);
			canvas.drawText("Std. dev: " + stdDev, 0, 34, text);
			canvas.drawText("Bin amount: " + m_histogram.getNumBins(), getWidth() * 0.4f, 34, text);
			
			m_histogram.size.x = (int)(getWidth() * 0.8f);
			m_histogram.size.y = (int)(getHeight() * 0.9f - m_histogram.pos.y);
			m_histogram.draw(canvas, green, absolute, labels, connectBars);
		}

		canvas.restore();

		/* Paint a image if we have it, just a demo, clean this up so it works
		 * your way, or remove it if you don't need it
		 */
		if (m_image != null) {
			Rect rect = new Rect(
					(int) (getWidth()*0.25F), (int) (getHeight()*0.25F), 
					(int) (getWidth()*0.75F), (int) (getHeight()*0.75F));
			Paint bmp = new Paint();
			bmp.setAntiAlias(true);
			bmp.setFilterBitmap(true);
			bmp.setDither(true);

			canvas.drawBitmap(m_image, null, rect, paint);
		}
	}

	/* Accessors */
	public void setSelectedImage(Bitmap image) {
		m_image = image;
	}

	public Bitmap getSelectedImage() {
		return m_image;
	}
	
	public void setBinSize(int sliderVal){
		m_histogram.setBinSize((int)Math.pow(2, 8 * (100 - sliderVal) / 100));
	}

	@Override
	public boolean onLongClick(View v) {
		connectBars = !connectBars;
		return true;
	}
}

