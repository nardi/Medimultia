/*
 * This class is used to create a Switch that allows the user to choose between
 * absolute values and relative values.
 */

package nl.uva.multimedia.imagehistogram;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

public class Absolute extends MySwitch {

	public Absolute(Context context) {
		super(context);
	}

	public Absolute(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Absolute(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton thingemejiggy, boolean isChecked) {
		switch_canvas.absolute= isChecked;
	}

}
