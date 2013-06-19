package nl.uva.multimedia.greenscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
/*
 * This class will create a popup that let's the user alter the settings for
 * the green screen, making blue screens or screens of any other colour possible.
 */
public class CalibrationFragment extends DialogFragment {
	private double hueCentre;
	private double hueLower;
	private double hueUpper;
	private double minSat;
	private double minVal;
	private double maxVal;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		/*
		 * Inflating the layout so it can find all the sliders.
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		View layoutId = getActivity().getLayoutInflater().inflate(R.layout.calibration_layout, null);
		builder.setView(layoutId);
		
		/*
		 * Creating the sliders and assigning values
		 */
		
		hueLower = GreenScreener.GREEN_HUE_LOWER;
		final ThresholdSlider hueLowSlider = (ThresholdSlider)layoutId.findViewById(R.id.hueLower);
		hueLowSlider.setMax(359);
		hueLowSlider.setProgress((int)Math.round(hueLower));
		
		hueCentre = GreenScreener.GREEN_HUE_CENTER;
		final ThresholdSlider hueCentSlider = (ThresholdSlider)layoutId.findViewById(R.id.hueCentre);
		hueCentSlider.setMax(359);
		hueCentSlider.setProgress((int) Math.round(hueCentre));
		
		hueUpper = GreenScreener.GREEN_HUE_UPPER;
		final ThresholdSlider hueUppSlider = (ThresholdSlider)layoutId.findViewById(R.id.hueUpper);
		hueUppSlider.setMax(359);
		hueUppSlider.setProgress((int)Math.round(hueUpper));
		
		minSat = GreenScreener.GREEN_MIN_SATURATION * 1000;
		final ThresholdSlider minSatSlider = (ThresholdSlider)layoutId.findViewById(R.id.minSat);
		minSatSlider.setMax(1000);
		minSatSlider.setProgress((int)Math.round(minSat));
		
		minVal = GreenScreener.GREEN_MIN_VALUE * 1000;
		final ThresholdSlider minValSlider = (ThresholdSlider)layoutId.findViewById(R.id.minVal);
		minValSlider.setMax(1000);
		minValSlider.setProgress((int)Math.round(minVal));
		
		
		maxVal = GreenScreener.GREEN_MAX_VALUE * 1000;
		final ThresholdSlider maxValSlider = (ThresholdSlider)layoutId.findViewById(R.id.maxVal);
		maxValSlider.setMax(1000);
		maxValSlider.setProgress((int)Math.round(maxVal));
		
		builder.setMessage("Calibration");
		/*
		 * Assigning an action to the 'Apply' button, in this case it is to save
		 * the values. 
		 */
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
			
			/*
			 * Cancel button that just closes the popup
			 */
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
			}
		});
		
		return builder.create();
	}

}
