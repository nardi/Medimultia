package nl.uva.multimedia.greenscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;

public class ThreshholdSlider extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	
	public float value;
	
	public ThreshholdSlider(Context context) {
		super(context);
	}

	public ThreshholdSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ThreshholdSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setup();
	}
	
	private void setup(){
		setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		Log.e("Slider","Progress is set to: " + progress);
		value = progress;
	}
	
	public void onStartTrackingTouch(SeekBar slider) { /* NOP */ }
	public void onStopTrackingTouch(SeekBar slider) { /* NOP */ }
}
