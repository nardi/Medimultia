/*
 * Simple slider used to set the feedback of the echo.
 * 
 * Nardi Lam and Bas Visser
 */

package nl.uva.multimedia.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

public class EchoFeedbackSlider extends MySlider {
	
	private EchoFilter echofilter;
	private double feedback = 0.5;
	private CanvasView canvas;

	public EchoFeedbackSlider(Context context) {
		super(context);
	}

	public EchoFeedbackSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EchoFeedbackSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */

		Log.e("Slider","Progress is set to: " + progress);
		feedback = 0.99 * progress / 100.0;
		if (echofilter != null) {
			echofilter.setFeedback(feedback);
		}
		
		if(canvas != null){
			canvas.setFeedback(feedback);
		}
		
		if(canvas != null){
			canvas.invalidate();
		}
	}
	
	public void setEchoFilter(EchoFilter echofilter){
		this.echofilter = echofilter;
		echofilter.setFeedback(feedback);
	}
	
	public void setCanvas(CanvasView canvas){
		this.canvas = canvas;
	}

}
