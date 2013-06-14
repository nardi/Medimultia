package nl.uva.multimedia.audio;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class EchoDelaySlider extends MySlider {
	
	private EchoFilter echofilter;
	private double delay = 1;
	private static CanvasView canvas;
	

	public EchoDelaySlider(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EchoDelaySlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EchoDelaySlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		//Log.i("Slider","Progress is set to: " + progress);
		delay = 0.01 + 1.99 * (100 - progress) / 100.0;
		if(echofilter != null){
			echofilter.setDelay(delay);
		}
		
		Log.v("Progress", "Delay set to: " + delay);
		canvas.setDelay(delay);
		canvas.drawDelay();
		
	}
	
	public void setEchoFilter(EchoFilter echofilter){
		this.echofilter = echofilter;
		echofilter.setDelay(delay);
	}
	
	public static void setCanvasView(CanvasView drawingCanvas){
		canvas = drawingCanvas;
	}

}
