package nl.uva.multimedia.imagehistogram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

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
	public void onClick(View view) {
		switch_canvas.absolute = !switch_canvas.absolute;
	}

}
