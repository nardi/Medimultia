/*
 * A simple slider used to set the delay after which the first echo is played.
 * 
 * Nardi Lam and Bas Visser
 */

package nl.uva.multimedia.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

public class EchoDelaySlider extends MySlider {
	
	private EchoFilter echofilter;
	private double delay = 1;
	private CanvasView canvas;
	
	public EchoDelaySlider(Context context) {
		super(context);
	}

	public EchoDelaySlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EchoDelaySlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		//Log.i("Slider","Progress is set to: " + progress);
		delay = 0.01 + 1.99 * (100 - progress) / 100.0;
		if (echofilter != null) {
			echofilter.setDelay(delay);
		}
		
		Log.v("Progress", "Delay set to: " + delay);
		if(canvas != null){
			canvas.setDelay((2 - delay) + 0.01);
		}
		
		if(canvas != null){
			canvas.invalidate();
		}
	}
	
	public void setEchoFilter(EchoFilter echofilter){
		this.echofilter = echofilter;
		echofilter.setDelay(delay);
	}
	
	public void setCanvas(CanvasView canvas){
		this.canvas = canvas;
	}
}
