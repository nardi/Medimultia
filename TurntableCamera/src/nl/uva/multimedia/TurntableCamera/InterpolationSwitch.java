package nl.uva.multimedia.TurntableCamera;

/*
 * This class is used to create a Switch that allows the user to choose
 * which type of interpolation is used when rotating the image.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

public class InterpolationSwitch extends MySwitch {
	CameraCapture cameraCapture = null;

	public InterpolationSwitch(Context context) {
		super(context);
	}

	public InterpolationSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterpolationSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton thingemejiggy, boolean isChecked) {
		if (cameraCapture != null)
			cameraCapture.setInterpolationMode(isChecked ?
					InterpolationMode.BILINEAR : InterpolationMode.NEAREST_NEIGHBOR);
	}

	public void setCameraCapture(CameraCapture c) {
		cameraCapture = c;
	}
}
