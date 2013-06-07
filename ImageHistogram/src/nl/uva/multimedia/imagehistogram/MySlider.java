package nl.uva.multimedia.imagehistogram;
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

class MySlider extends SeekBar implements SeekBar.OnSeekBarChangeListener {
	public CanvasView canvas_view = null;
	
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

	/* Push new bar size to Histogram on Slider change */
	public void onProgressChanged(SeekBar slider, int progress,
			boolean from_user) {
		canvas_view.setBinSize(progress);
	}

	public void onStartTrackingTouch(SeekBar slider) { /* NOP */ }
	public void onStopTrackingTouch(SeekBar slider) { /* NOP */ }
}
