/* Computer Graphics, Assignment 1, Bresenham's Midpoint Line-Algorithm
 *
 * Filename ........ mla.c
 * Description ..... Midpoint Line Algorithm
 * Created by ...... Jurgen Sturm 
 *
 * Student name .... Bas Visser / Nardi Lam
 * Student email ... bas.visser2@student.uva.nl / mij@nardilam.nl
 * Collegekaart .... 10439013 / 10453555
 * Date ............ 2014/02/07
 * Comments ........ 10/10 would let Rex check my work again
 *
 * (always fill in these fields before submitting!!)
 */

#include "SDL.h"   
#include "init.h"

/*
 * Midpoint Line Algorithm
 *
 * As you probably will have figured out, this is the part where you prove
 * your programming skills. The code in the mla function should draw a direct 
 * line between (x0,y0) and (x1,y1) in the specified color. 
 * 
 * Until now, the example code below draws only a horizontal line between
 * (x0,y0) and (x1,y0) and a vertical line between (x1,y1).
 * 
 * And here the challenge begins..
 *
 * Good luck!
 *
 *
 */

void swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void mla(SDL_Surface *s, int x0, int y0, int x1, int y1, Uint32 colour) {
    int iy;
    int dx, dy;

    PutPixel(s,x0,y0,colour);
    
    dx = abs(x1 - x0);
    dy = abs(y1 - y0);
    
    // Swap x and y if dy is larger, i.e. multiple y-values needed per x to form something resembling a line
    int swap_axes = dy > dx;
    if (swap_axes) {
        swap(&x0, &y0);
        swap(&x1, &y1);
        swap(&dx, &dy);
    }
    
    // Normalize x-direction
    if (x1 < x0) {
        swap(&x0, &x1);
        swap(&y0, &y1);
    }
    
    // For every x-step, add the corresponding amount of y to error, and round
    // it to the nearest integer to get the matching y-coordinate.
    // We multiply all values by dx to avoid floating-point arithmetic.
    int slope = dy; // dx * (dy / dx);
    int error = 0;
    int rounding_threshold = dx / 2; // dx * (1/2);

    if (y0 < y1) iy = 1; else iy = -1;

    while (x0 != x1) {
        x0++;
        
        error += slope;
        if (error > rounding_threshold) {
            y0 += iy;
            error -= dx; // dx * 1;
        }
            
        if (swap_axes) {
            PutPixel(s,y0,x0,colour);
        } else {
            PutPixel(s,x0,y0,colour);
        }  
    }
    
    return;
}

