package nl.uva.multimedia.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

public class EchoFeedbackSlider extends MySlider {
	
	private EchoFilter echofilter;
	private double feedback = 0.5;

	public EchoFeedbackSlider(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EchoFeedbackSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EchoFeedbackSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		Log.e("Slider","Progress is set to: " + progress);
		feedback = 0.99 * progress / 100.0;
		if(echofilter != null){
			echofilter.setFeedback(feedback);
		}
	}
	
	public void setEchoFilter(EchoFilter echofilter){
		this.echofilter = echofilter;
		echofilter.setFeedback(feedback);
	}

}
