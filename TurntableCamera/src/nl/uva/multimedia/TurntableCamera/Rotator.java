package nl.uva.multimedia.TurntableCamera;

/*
 * Rotators gonna rotate
 */

import android.graphics.Point;
import android.graphics.PointF;
import nl.uva.multimedia.TurntableCamera.InterpolationMode;

public class Rotator {

	public static void rotate(int[] dest, int[] src, int width, int height,
			double angle, InterpolationMode intMode) {
		//TODO Deze 1 keer laten alloceren.
		PointF center = new PointF(width/2f, height/2f);
		Point destPixel = new Point();
		PointF destPoint = new PointF();
		PointF srcPoint = new PointF();
		Point srcPixel = new Point();
		
		Point lowerLeft = new Point();
		Point lowerRight = new Point();
		Point upperLeft = new Point();
		Point upperRight = new Point();
		
		double lowerColour;
		double upperColour;
		double colour;
		
		float cos = (float)Math.cos(angle), sin = (float)Math.sin(angle);
		
		for (destPixel.x = 0; destPixel.x < width; destPixel.x++) {
			for (destPixel.y = 0; destPixel.y < height; destPixel.y++) {
				int destPixelLocation = destPixel.x + destPixel.y * width;

				destPoint.x = destPixel.x - center.x;
				destPoint.y = destPixel.y - center.y;
				
				srcPoint.x = destPoint.x * cos - destPoint.y * sin;
				srcPoint.y = destPoint.x * sin + destPoint.y * cos;

				/*subPixel interpoleren naar srcPixel (Nearest neighbour) */
				srcPixel.x = Math.round(srcPoint.x + center.x);
				srcPixel.y = Math.round(srcPoint.y + center.y);
				
				/*Bilinear interpolation */
				//TODO helft kan weg
				if(intMode == InterpolationMode.BILINEAR){
					lowerLeft.x = (int) srcPoint.x;
					lowerLeft.y = (int)	srcPoint.y;
					
					upperLeft.x = (int) srcPoint.x;
					upperLeft.y = (int) srcPoint.y + 1;
					
					lowerRight.x = (int) srcPoint.x + 1;
					lowerRight.y = (int) srcPoint.y;
					
					upperRight.x = (int)srcPoint.x + 1;
					upperRight.y = (int)srcPoint.y + 1;		
					
					lowerColour = ((lowerRight.x - srcPoint.x) * getColour(src,lowerLeft, width))+ ((srcPoint.x - lowerLeft.x) * getColour(src, lowerRight, width));
					upperColour = ((lowerRight.x - srcPoint.x) * getColour(src,upperLeft, width)) +((srcPoint.x - lowerLeft.x) * getColour(src, upperRight, width));
					
					colour = (int)((upperLeft.y - srcPoint.y) * lowerColour) + ((upperRight.y - srcPoint.y) * upperColour);
				}
				// Niet meer Bilinear interpolation
				
				if (srcPixel.x < 0 || srcPixel.x >= width
				 || srcPixel.y < 0 || srcPixel.y >= height) {
					dest[destPixelLocation] = 0;
				} else {
					int srcPixelLocation = srcPixel.x + srcPixel.y * width;
					dest[destPixelLocation] = src[srcPixelLocation];
				}					
			}
		}
	}
	
	public static int getColour(int[] src, Point toGet, int width){
		return src[toGet.x + toGet.y * width];
	}
}
