package nl.uva.multimedia.TurntableCamera;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
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
	}
	
	public MySlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	{
		setOnSeekBarChangeListener(this);
	}

	/* Push new bar size to HistogramView on Slider change */
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {

		/* Do something with progress here */
		Log.i("Slider","Progress is set to: " + progress);
	}

	public void onStartTrackingTouch(SeekBar slider) { /* NOP */ }
	public void onStopTrackingTouch(SeekBar slider) { /* NOP */ }
}
