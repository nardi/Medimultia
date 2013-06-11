package nl.uva.multimedia.TurntableCamera;

/*
 * Rotators gonna rotate
 */

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import nl.uva.multimedia.TurntableCamera.InterpolationMode;

public class Rotator {
	PointF center = new PointF();
	Point destPixel = new Point();
	PointF destPoint = new PointF();
	PointF srcPoint = new PointF();
	Point srcPixel = new Point();
	
	Point lowerLeft = new Point();
	Point lowerRight = new Point();
	Point upperLeft = new Point();
	Point upperRight = new Point();
	
	public void rotate(int[] dest, int[] src, int width, int height,
			double angle, InterpolationMode intMode) {
		center.x = width / 2f;
		center.y = height / 2f;
		
		float cos = (float)Math.cos(-angle), sin = (float)Math.sin(-angle);
		
		for (destPixel.x = 0; destPixel.x < width; destPixel.x++) {
			for (destPixel.y = 0; destPixel.y < height; destPixel.y++) {
				int destPixelLocation = destPixel.x + destPixel.y * width;

				destPoint.x = destPixel.x - center.x;
				destPoint.y = destPixel.y - center.y;
				
				srcPoint.x = destPoint.x * cos - destPoint.y * sin + center.x;
				srcPoint.y = destPoint.x * sin + destPoint.y * cos + center.y;

				int colour = 0x00000000;
				
				if (srcPoint.x >= 0 && srcPoint.x < width - 1
				 && srcPoint.y >= 0 && srcPoint.y < height - 1) {
					switch (intMode) {
						case BILINEAR:							
							lowerLeft.x = (int)srcPoint.x;
							lowerLeft.y = (int)srcPoint.y;
							
							upperLeft.x = (int)srcPoint.x;
							upperLeft.y = (int)srcPoint.y + 1;
							
							lowerRight.x = (int)srcPoint.x + 1;
							lowerRight.y = (int)srcPoint.y;
							
							upperRight.x = (int)srcPoint.x + 1;
							upperRight.y = (int)srcPoint.y + 1;		
							
							float xFactor = lowerRight.x - srcPoint.x;
							int lowerColour = interpolateColour(getColour(src, lowerLeft, width),
									xFactor, getColour(src, lowerRight, width));
							int upperColour = interpolateColour(getColour(src, upperLeft, width),
									xFactor, getColour(src, upperRight, width));
							
							colour = interpolateColour(lowerColour, 
									upperLeft.y - srcPoint.y, upperColour);
							break;

						case NEAREST_NEIGHBOR:
							srcPixel.x = Math.round(srcPoint.x);
							srcPixel.y = Math.round(srcPoint.y);
							colour = src[srcPixel.x + srcPixel.y * width];
							break;
					}
				}

				dest[destPixelLocation] = colour;
			}
		}
	}
	
	private int interpolateColour(int colour1, double factor1, int colour2) {
		int red1 = Color.red(colour1), green1 = Color.green(colour1),
			blue1 = Color.blue(colour1);
		int red2 = Color.red(colour2), green2 = Color.green(colour2),
			blue2 = Color.blue(colour2);
		
		int red = (int)(red1 * factor1 + red2 * (1 - factor1));
		int green = (int)(green1 * factor1 + green2 * (1 - factor1));
		int blue = (int)(blue1 * factor1 + blue2 * (1 - factor1));
		
		return Color.rgb(red, green, blue);
	}
	
	private int getColour(int[] src, Point toGet, int width){
		return src[toGet.x + toGet.y * width];
	}
}
