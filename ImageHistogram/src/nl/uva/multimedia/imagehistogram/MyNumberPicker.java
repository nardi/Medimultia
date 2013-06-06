package nl.uva.multimedia.imagehistogram;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;

public class MyNumberPicker extends NumberPicker implements OnScrollListener,
		OnValueChangeListener {
	
	public 
	
	public MyNumberPicker(Context context) {
		super(context);
	}

	public MyNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChange(NumberPicker arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
