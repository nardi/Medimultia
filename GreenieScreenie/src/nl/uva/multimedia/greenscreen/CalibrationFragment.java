package nl.uva.multimedia.greenscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class CalibrationFragment extends DialogFragment {
	private double hueCentre;
	private double hueLower;
	private double hueUpper;
	private double minSat;
	private double minVal;
	private double maxVal;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		View layoutId = getActivity().getLayoutInflater().inflate(R.layout.calibration_layout, null);
		builder.setView(layoutId);
		
		hueLower = GreenScreener.GREEN_HUE_LOWER;
		final ThreshholdSlider hueLowSlider = (ThreshholdSlider)layoutId.findViewById(R.id.hueLower);
		hueLowSlider.setMax(359);
		hueLowSlider.setProgress((int)Math.round(hueLower));
		
		hueCentre = GreenScreener.GREEN_HUE_CENTER;
		final ThreshholdSlider hueCentSlider = (ThreshholdSlider)layoutId.findViewById(R.id.hueCentre);
		hueCentSlider.setMax(359);
		hueCentSlider.setProgress((int) Math.round(hueCentre));
		
		hueUpper = GreenScreener.GREEN_HUE_UPPER;
		final ThreshholdSlider hueUppSlider = (ThreshholdSlider)layoutId.findViewById(R.id.hueUpper);
		hueUppSlider.setMax(359);
		hueUppSlider.setProgress((int)Math.round(hueUpper));
		
		minSat = GreenScreener.GREEN_MIN_SATURATION * 1000;
		final ThreshholdSlider minSatSlider = (ThreshholdSlider)layoutId.findViewById(R.id.minSat);
		minSatSlider.setMax(1000);
		minSatSlider.setProgress((int)Math.round(minSat));
		
		minVal = GreenScreener.GREEN_MIN_VALUE * 1000;
		final ThreshholdSlider minValSlider = (ThreshholdSlider)layoutId.findViewById(R.id.minVal);
		minValSlider.setMax(1000);
		minValSlider.setProgress((int)Math.round(minVal));
		
		
		maxVal = GreenScreener.GREEN_MAX_VALUE * 1000;
		final ThreshholdSlider maxValSlider = (ThreshholdSlider)layoutId.findViewById(R.id.maxVal);
		maxValSlider.setMax(1000);
		maxValSlider.setProgress((int)Math.round(maxVal));
		
		builder.setMessage("Calibration");
		builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				GreenScreener.GREEN_HUE_CENTER = hueCentSlider.value;
				GreenScreener.GREEN_HUE_LOWER = hueLowSlider.value;				
				GreenScreener.GREEN_HUE_UPPER = hueUppSlider.value;
				GreenScreener.GREEN_MIN_SATURATION = minSatSlider.value / 1000;
				GreenScreener.GREEN_MIN_VALUE = minValSlider.value / 1000;
				GreenScreener.GREEN_MAX_VALUE = maxValSlider.value /1000;
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
			}
		});
		
		return builder.create();
	}

}
