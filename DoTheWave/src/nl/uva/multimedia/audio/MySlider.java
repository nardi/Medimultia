package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 * 
 * Basis for the sliders we used.
 * 
 * Nardi Lam and Bas Visser
 */

/* XXX Yes, you should change stuff here */

import android.content.Context;
import android.widget.SeekBar;
import android.util.AttributeSet;
import android.util.Log;

class MySlider extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	
	/* Necessary constructors */
	public MySlider(Context context) {
		super(context);
		setup();
	}
	
	public MySlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public MySlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	private void setup() {
		setOnSeekBarChangeListener(this);
	}
	

	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		Log.e("Slider","Progress is set to: " + progress);
	}

	public void onStartTrackingTouch(SeekBar slider) { /* NOP */ }
	public void onStopTrackingTouch(SeekBar slider) { /* NOP */ }
}
