package nl.uva.multimedia.imagehistogram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class Labels extends MySwitch {

	public Labels(Context context) {
		super(context);
	}

	public Labels(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Labels(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onClick(View view) {
		switch_canvas.labels = !switch_canvas.labels;
	}

}
