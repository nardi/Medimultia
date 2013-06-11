/*
 * Switch class that is used to create toggle-switches for switching between
 * absolute and relative values and to toggle the labels.
 */

package nl.uva.multimedia.TurntableCamera;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public abstract class MySwitch extends Switch implements OnCheckedChangeListener {
	CanvasView switch_canvas = null;

	public MySwitch(Context context) {
		super(context);
	}

	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOnCheckedChangeListener(this);
	}
}
