package nl.uva.multimedia.TurntableCamera;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.util.AttributeSet;

public class CanvasView extends View {
	private int[] argb = null;
	private int image_width;
	private int image_height;

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

		if (argb != null) {
			canvas.drawBitmap(argb, 0, image_width, (getWidth() - image_width) / 2,
					(getHeight() - image_height) / 2, image_width, image_height, false, null);
		}
	}
	
	public void setImage(int[] argb, int width, int height) {
		this.argb = argb;
		image_width = width;
		image_height = height;
	}
}
