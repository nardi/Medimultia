/*
 * Slider to set the different thresholds for the GreenScreener class
 */

package nl.uva.multimedia.greenscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class ThresholdSlider extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	
	public float value;
	
	public ThresholdSlider(Context context) {
		super(context);
	}

	public ThresholdSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ThresholdSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		value = progress;
	}
	
	public void onStartTrackingTouch(SeekBar slider) { /* NOP */ }
	public void onStopTrackingTouch(SeekBar slider) { /* NOP */ }
}
