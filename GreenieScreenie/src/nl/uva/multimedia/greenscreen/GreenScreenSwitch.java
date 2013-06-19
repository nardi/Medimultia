package nl.uva.multimedia.greenscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class GreenScreenSwitch extends Switch implements
		OnCheckedChangeListener {
	
	public CanvasView canvas;
	
	public GreenScreenSwitch(Context context) {
		super(context);
	}

	public GreenScreenSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GreenScreenSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		canvas.doGreenScreen = arg1;
		canvas.invalidate();
	}

}
