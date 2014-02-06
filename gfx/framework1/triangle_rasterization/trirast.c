/* Computer Graphics assignment, Triangle Rasterization
 * Filename ........ trirast.c
 * Description ..... Implements triangle rasterization
 * Created by ...... Paul Melis
 *
 * Student name ....
 * Student email ...
 * Collegekaart ....
 * Date ............
 * Comments ........
 *
 *
 * (always fill in these fields before submitting!!)
 */

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "types.h"

/*
 * Rasterize a single triangle.
 * The triangle is specified by its corner coordinates
 * (x0,y0), (x1,y1) and (x2,y2).
 * The triangle is drawn in color (r,g,b).
 */

float f20(float x, float y, float x0, float y0, float x2, float y2){
	return (y2 - y0) * x + (x0 - x2) * y + x2 * y0 - x0 * y2;
}

float f01(float x, float y, float x1, float y1, float x0, float y0){
	return (y0 - y1) * x + (x1 - x0) * y + x0 * y1 - x1 * y0;
}

void
draw_triangle(float x0, float y0, float x1, float y1, float x2, float y2,
    byte r, byte g, byte b) {
		int ymin = (int)fmin(fmin(y0, y1), y2);
		int ymax = (int)fmax(fmax(y0, y1), y2);

		int xmin = (int)fmin(fmin(x0,x1),x2);
		int xmax = (int)fmax(fmax(x0, x1),x2);
		
		float beta, alpha, gamma;

		for(int y = ymin; y <= ymax; y++){
			for(int x = xmin; x <= xmax; x++){
				beta = f20(x, y, x0 ,y0, x2, y2) / f20(x1, y1, x0, y0, x2, y2);
				gamma = f01(x, y, x1, y1, x0, y0) / f01(x2, y2, x1, y1, x0, y0);
				alpha = 1 - beta - gamma;
				if (alpha > 0 && beta > 0 && gamma > 0){
				// dan ook < 1
					
					//PutPixel(x, y, alpha * r, beta * g, gamma *b);
					PutPixel(x, y, r, g, b);
				}
			}		
		}

    
}

void
draw_triangle_optimized(float x0, float y0, float x1, float y1, float x2, float y2,
    byte r, byte g, byte b) {
    
}
