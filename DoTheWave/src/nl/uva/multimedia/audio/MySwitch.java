package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX Yes, you should change stuff here */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MySwitch extends Switch implements Switch.OnCheckedChangeListener {
	
	public MySwitch(Context context) {
		super(context);
		setup();
	}

	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	private void setup() {
		this.setOnCheckedChangeListener(this);
		this.setText("MySwitch");
		this.setTextOff("Off");
		this.setTextOn("On");
		
	}


	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		Log.e("MySwitch","State is now: " + arg1);
	}

}
