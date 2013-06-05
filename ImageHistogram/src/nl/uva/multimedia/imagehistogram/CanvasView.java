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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.view.View;
import android.util.AttributeSet;

import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;

public class CanvasView extends View {
	public int[] argb;
	public int image_width;
	public int image_height;
	public int binSize = 0;
	
	Bitmap m_image = null;
	Histogram m_histogram = null;

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

		
				
		canvas.drawColor(Color.BLACK);
	
		/* Define the basic paint */
		Paint paint = new Paint();
		paint.setColor(Color.rgb(0xa0,0xa0,0xb0));
		paint.setAntiAlias(true);
	
		/* text inherits from the basic paint */
		Paint text = new Paint(paint);
		text.setColor(Color.WHITE);
		text.setShadowLayer(3.0F,3.0F,3.0F,Color.rgb(0x20,0x20,0x20));
		text.setTextSize(getHeight() * 0.1F);
	
		/* Save state */
		canvas.save();
		canvas.translate(getWidth() * 0.1F, getHeight() * 0.1F);

		//canvas.drawText("Hello world! ", 0.0F, 0.0F, text);
		
		Paint greenVal = new Paint(text);
		greenVal.setColor(Color.RED);
		
		if(argb != null && image_width > 0 && image_height > 0){
			canvas.drawText("Greenvalue UL: " + argb[0], 0.0F, 0, greenVal);
			canvas.drawText("Greenvalue MID: " + argb[image_width/2 * image_height/2],0,40,greenVal);
			canvas.drawText("Greenvalue LR: " + argb[image_width * image_height - 1],0,80,greenVal);
			canvas.drawText("Binsize: " + binSize, 0, 100, y, paint)
			m_histogram = new Histogram(new Point(0, 110), new Point(380, 180), 255, 20);
			m_histogram.draw(canvas, argb);
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
		binSize = 1 + 254 * sliderVal / 100;
		m_histogram.setNumBins(binSize);
		
	}
	
}

