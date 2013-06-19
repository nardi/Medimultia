package nl.uva.multimedia.greenscreen;

/*
 * The class that actually applies the green screen effect.
 */

import android.graphics.Color;

public class GreenScreener {
	public static float GREEN_HUE_CENTER = 120;
	public static float GREEN_HUE_LOWER = 60;
	public static float GREEN_HUE_UPPER = 180;
	public static float GREEN_MIN_SATURATION = 0.15f;
	public static float GREEN_MIN_VALUE = 0.25f;
	public static float GREEN_MAX_VALUE = 0.9f;
	
	private static final float[] hsv = new float[3];

	public static void screen(int[] argb) {
		for (int i = 0; i < argb.length; i++) {
			Color.colorToHSV(argb[i], hsv);
			if (hsv[0] >= GREEN_HUE_LOWER
			 && hsv[0] <= GREEN_HUE_UPPER
			 && hsv[1] >= GREEN_MIN_SATURATION
			 && hsv[2] >= GREEN_MIN_VALUE
			 && hsv[2] <= GREEN_MAX_VALUE) {
				float hueAmount = 0;
				if (hsv[0] > GREEN_HUE_CENTER) {
					hueAmount = (hsv[0] - GREEN_HUE_CENTER)
							/ (GREEN_HUE_UPPER - GREEN_HUE_CENTER);
				}
				else {
					hueAmount = (GREEN_HUE_CENTER - hsv[0])
							/ (GREEN_HUE_CENTER - GREEN_HUE_LOWER);
				}
				int alpha = (int)(50 * hueAmount);
				argb[i] &= alpha << 24 | 0x00FFFFFF;
			}
		}
	}
}
