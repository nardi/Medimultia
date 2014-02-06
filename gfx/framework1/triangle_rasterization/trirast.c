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

#define NIETES 0
#define INDERDAAD 1

/*
 * Rasterize a single triangle.
 * The triangle is specified by its corner coordinates
 * (x0,y0), (x1,y1) and (x2,y2).
 * The triangle is drawn in color (r,g,b).
 */

double f20(float x, float y, float x0, float y0, float x2, float y2){
	return (y2 - y0) * x + (x0 - x2) * y + (x2 * y0) - (x0 * y2);
}

double f01(float x, float y, float x1, float y1, float x0, float y0){
	return (y0 - y1) * x + (x1 - x0) * y + (x0 * y1) - (x1 * y0);
}

double f12(float x, float y, float x1, float y1, float x2, float y2){
	return (y1 - y2) * x + (x2 - x1) * y + (x1 * y2) - (x2 * y1);
}

void
draw_triangle(float x0, float y0, float x1, float y1, float x2, float y2,
    byte r, byte g, byte b) {
	/*PutPixel(x0, y0, r, g, b);
	PutPixel(x1, y1, r, g, b);
	PutPixel(x2, y2, r, g, b);*/

	int ymin = (int)floor(fmin(fmin(y0, y1), y2));
	int ymax = (int)ceil(fmax(fmax(y0, y1), y2));

	int xmin = (int)floor(fmin(fmin(x0,x1),x2));
	int xmax = (int)ceil(fmax(fmax(x0, x1),x2));
	
	double fbeta, beta, falpha, alpha, fgamma, gamma;
	
	falpha = f12(x0, y0, x1, y1, x2, y2);
	fbeta = f20(x1, y1, x0, y0, x2, y2);
	fgamma = f01(x2, y2, x1, y1, x0, y0);
		

	for(int y = ymin; y <= ymax; y++){
		for(int x = xmin; x <= xmax; x++){
			beta = f20(x, y, x0 ,y0, x2, y2) /fbeta;
			gamma = f01(x, y, x1, y1, x0, y0) / fgamma;
			alpha = f12(x, y, x1, y1, x2, y2) / falpha;
			if (alpha >= 0 && beta >= 0 && gamma >= 0){
				if((alpha > 0 || falpha * f12(-1,-1,x1,y1,x2,y2) > 0) &&
				   (beta > 0 || fbeta * f20(-1,-1,x0,y0,x2,y2) > 0) &&
				   (gamma > 0 || fgamma * f01(-1,-1,x1,y1,x0,y0) > 0)){
				//PutPixel(x, y, alpha * r, beta * g, gamma *b);
				PutPixel(x, y, r, g, b);
				}
			}
		}		
	}
}

void
draw_triangle_optimized(float x0, float y0, float x1, float y1, float x2, float y2,
    byte r, byte g, byte b) {	
	int bas_was_here;	

	int ymin = (int)floor(fmin(fmin(y0, y1), y2));
	int ymax = (int)ceil(fmax(fmax(y0, y1), y2));

	int xmin = (int)floor(fmin(fmin(x0,x1),x2));
	int xmax = (int)ceil(fmax(fmax(x0, x1),x2));
	
	double offbeta, fbeta, ybeta, offalpha, falpha, yalpha, offgamma, fgamma, ygamma;
	
	falpha = f12(x0, y0, x1, y1, x2, y2);
	fbeta = f20(x1, y1, x0, y0, x2, y2);
	fgamma = f01(x2, y2, x1, y1, x0, y0);
		
	offalpha = falpha * f12(-1,-1,x1,y1,x2,y2);
	offbeta = fbeta * f20(-1,-1,x0,y0,x2,y2);
	offgamma = fgamma * f01(-1,-1,x1,y1,x0,y0);

	// Incremental
	//ybeta = f20(xmin, ymin, x0 ,y0, x2, y2) / fbeta;
	//ygamma = f01(xmin, ymin, x1, y1, x0, y0) / fgamma;
	//yalpha = f12(xmin, ymin, x1, y1, x2, y2) / falpha;

	float alpha, beta, gamma;

	for(int y = ymin; y <= ymax; y++){
		bas_was_here = NIETES;
		
		// Incremental
		//alpha = yalpha;
		//beta = ybeta;
		//gamma = ygamma;

		for(int x = xmin; x <= xmax; x++){
			// Non-incremental
			beta = f20(x, y, x0 ,y0, x2, y2) /fbeta;
			gamma = f01(x, y, x1, y1, x0, y0) / fgamma;
			alpha = f12(x, y, x1, y1, x2, y2) / falpha;

			if (alpha >= 0 && beta >= 0 && gamma >= 0) {
				if((alpha > 0 || offalpha > 0) &&
				   (beta > 0 ||  offbeta > 0) &&
				   (gamma > 0 ||  offgamma > 0)) {
					bas_was_here = INDERDAAD;				
					PutPixel(x, y, r, g, b);
				}
			} else if(bas_was_here) {
				break;
			}

			// Incremental
			//beta += (y2 - y0)/fbeta;
			//gamma += (y0 - y1)/fgamma;
			//alpha += (y1 - y2)/falpha;
		}
		
		// Incremental
		//ybeta += (x0 - x2)/fbeta;
		//ygamma += (x1 - x0)/fgamma;
		//yalpha += (x2 - x1)/falpha;
	}
}
