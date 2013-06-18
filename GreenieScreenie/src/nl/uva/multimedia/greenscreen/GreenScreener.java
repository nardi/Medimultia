package nl.uva.multimedia.greenscreen;

import android.graphics.Color;

public class GreenScreener {
	private static final float GREEN_HUE_CENTER = 120;
	private static final float GREEN_HUE_LOWER = 60;
	private static final float GREEN_HUE_UPPER = 180;
	private static final float GREEN_MIN_SATURATION = 0.15f;
	private static final float GREEN_MIN_VALUE = 0.3f;
	
	private static final float[] hsv = new float[3];

	public static void screen(int[] argb) {
		for (int i = 0; i < argb.length; i++) {
			Color.colorToHSV(argb[i], hsv);
			//float chroma = hsv[1] * hsv[2];
			if (hsv[0] >= GREEN_HUE_LOWER
			 && hsv[0] <= GREEN_HUE_UPPER
			 && hsv[1] >= GREEN_MIN_SATURATION
			 && hsv[2] >= GREEN_MIN_VALUE) {
			 //&& chroma >= GREEN_MIN_CHROMA) {
				
				float hueAmount = 0;
				if (hsv[0] > GREEN_HUE_CENTER) {
					hueAmount = (hsv[0] - GREEN_HUE_CENTER)
							/ (GREEN_HUE_UPPER - GREEN_HUE_CENTER);
				}
				else {
					hueAmount = (GREEN_HUE_CENTER - hsv[0])
							/ (GREEN_HUE_CENTER - GREEN_HUE_LOWER);
				}
				//float chromaAmount = (chroma - GREEN_MIN_CHROMA) / (1 - GREEN_MIN_CHROMA);
				//int alpha = (int)(90 * hueAmount * chromaAmount);
				int alpha = (int)(50 * hueAmount);
				argb[i] &= alpha << 24 | 0x00FFFFFF;
			}
		}
	}
}
