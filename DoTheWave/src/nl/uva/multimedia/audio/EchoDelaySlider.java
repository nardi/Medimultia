package nl.uva.multimedia.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

public class EchoDelaySlider extends MySlider {
	
	public EchoFilter echofilter;
	//Dit is commentaar om GIT te testen.

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
		Log.e("Slider","Progress is set to: " + progress);
		float delay = progress/100 * 2;
		if(echofilter != null){
			echofilter.setDelay(delay);
		}
	}
	
	public void setEchoFilter(EchoFilter echofilter){
		this.echofilter = echofilter;
	}

}
