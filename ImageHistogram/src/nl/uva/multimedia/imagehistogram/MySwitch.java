package nl.uva.multimedia.imagehistogram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.Switch;

public abstract class MySwitch extends Switch implements OnClickListener {
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
		setOnClickListener(this);
	}
}
