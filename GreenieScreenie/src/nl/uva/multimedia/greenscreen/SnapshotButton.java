package nl.uva.multimedia.greenscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.view.View;

public class SnapshotButton extends Button implements View.OnClickListener {
	
	public CanvasView canvas;
		
	public SnapshotButton(Context context) {
		super(context);
	}

	public SnapshotButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SnapshotButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		canvas.invalidate();		
	}

}
