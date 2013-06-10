package nl.uva.multimedia.TurntableCamera;

/*
 * Rotators gonna rotate
 */

import android.graphics.Point;
import android.graphics.PointF;

public class Rotator {
	public static void rotate(int[] dest, int[] src, int width, int height, float angle) {
		PointF center = new PointF(width/2f, height/2f);
		Point destPixel = new Point();
		PointF destPoint = new PointF();
		PointF srcPoint = new PointF();
		Point srcPixel = new Point();
		
		float cos = (float)Math.cos(angle), sin = (float)Math.sin(angle);
		
		for (destPixel.x = 0; destPixel.x < width; destPixel.x++) {
			for (destPixel.y = 0; destPixel.y < height; destPixel.y++) {
				destPoint.x = destPixel.x - center.x;
				destPoint.y = destPixel.y - center.y;

				srcPoint.x = destPoint.x * cos - destPoint.y * sin;
				srcPoint.y = destPoint.x * sin + destPoint.y * cos;

				// subPixel interpoleren naar srcPixel
				srcPixel.x = Math.round(srcPoint.x + center.x);
				srcPixel.y = Math.round(srcPoint.y + center.y);

				int srcPixelLocation = srcPixel.x + srcPixel.y * width;
				int destPixelLocation = destPixel.x + destPixel.y * width;

				if (srcPixelLocation >= 0 && srcPixelLocation < src.length)
					dest[destPixelLocation] = src[srcPixelLocation];
				else
					dest[destPixelLocation] = 0;
			}
		}
	}
}
