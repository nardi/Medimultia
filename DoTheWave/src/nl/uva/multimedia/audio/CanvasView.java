package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 * The canvas on which this app is drawn.
 */

/* XXX Yes, you should change stuff here */

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CanvasView extends View {
	
	private double curDelay;
	private double curFeedback;
	private String file;
	private int fileStart;
	private String fileToDraw;
	DecimalFormat df = new DecimalFormat("#.##");
	
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
	
		canvas.drawText("Delay: " + df.format(curDelay) +"s", 10F , 50F, text);
		canvas.drawText("Echo Amplification " + df.format(curFeedback) , 10F, 80F, text);
		if(file != null){
			fileStart = fileStart = file.lastIndexOf("/");
			fileToDraw = file.substring(fileStart + 1);
			canvas.drawText("File: " + fileToDraw, 10F, 110F, text);
		}
		
		else{
			canvas.drawText("No file chosen", 10F, 110F, text);
		}
		/* Save state */
		/*canvas.save();
		canvas.translate(getWidth() * 0.1F, getHeight() * 0.1F);

		canvas.drawText("Hello world! ", 0.0F, 0.0F, text);
		
		canvas.restore();*/
	}
	
	public void setDelay(double delay){
		curDelay = delay;
	}
	
	public void setFeedback(double feedback){
		curFeedback = feedback;
	}
	
	public void setFile(String file){
		this.file = file;
	}
}