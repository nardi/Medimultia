package nl.uva.multimedia.greenscreen;
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
import android.view.View;
import android.util.AttributeSet;

import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;

public class CanvasView extends View {
	public int[] argb;
	public int[] yuvComponents;
	public float[] hsv;
	public int image_width;
	public int image_height;
	
	public boolean yuv;
	
	Bitmap m_image = null;
	Histogram histogram = new HSVHistogram(new Point(0, 5), new Point(0, 0));

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

		/* Paint an image if we have it, just a demo, clean this up so it works
		 * your way, or remove it if you don't need it
		 */
		if (m_image != null) {
			/* Define the basic paint */
			Paint paint = new Paint();
			paint.setColor(Color.rgb(0xa0,0xa0,0xb0));
			paint.setAntiAlias(true);
			
			Rect rect = new Rect(
					(int) (getWidth()*0.25F), (int) (getHeight()*0.25F), 
					(int) (getWidth()*0.75F), (int) (getHeight()*0.75F));
			Paint bmp = new Paint();
			bmp.setAntiAlias(true);
			bmp.setFilterBitmap(true);
			bmp.setDither(true);


			canvas.drawBitmap(m_image, null, rect, paint);
		}
		
		if (hsv != null) {
			histogram.position.x = (getWidth() - image_width) / 2;
			histogram.setSize(image_width, Math.min(getHeight() - 30, image_height));
			histogram.draw(canvas, hsv);
		}
	}

	/* Accessors */
	public void setSelectedImage(Bitmap image) {
		m_image = image;
	}

	public Bitmap getSelectedImage() {
		return m_image;
	}
}

