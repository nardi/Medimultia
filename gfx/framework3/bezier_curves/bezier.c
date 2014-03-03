/* Computer Graphics, Assignment, Bezier curves
 * Filename ........ bezier.c
 * Description ..... Bezier curves
 * Date ............ 22.07.2009
 * Created by ...... Paul Melis
 *
 * Student name .... Bas Visser / Nardi Lam
 * Student email ... bas.visser2@student.uva.nl / mij@nardilam.nl
 * Collegekaart .... 10439013 / 10453555
 * Date ............ 2014/02/21
 * Comments ........
 *
 *
 * (always fill in these fields before submitting!!)
 */

#include <math.h>
#include "bezier.h"
#include <stdio.h>
#include <float.h>

/* Given a Bezier curve defined by the 'num_points' control points
 * in 'p' compute the position of the point on the curve for parameter
 * value 'u'.
 *
 * Return the x and y values of the point by setting *x and *y,
 * respectively.
 */

void evaluate_bezier_curve(float *x, float *y, control_point p[], int num_points, float u) {
    *x = 0; *y = 0;
    for(int i = 0; i < num_points; i++){
        float b = B(i, num_points - 1, u);
        *x += b * p[i].x;
        *y += b * p[i].y;
    }
}

/* Draw a Bezier curve defined by the control points in p[], which
 * will contain 'num_points' points.
 *
 * Draw the line segments you compute directly on the screen
 * as a single GL_LINE_STRIP. This is as simple as using
 *
 *      glBegin(GL_LINE_STRIP);
 *      glVertex2f(..., ...);
 *      ...
 *      glEnd();
 *
 * DO NOT SET ANY COLOR!
 *
 * The 'num_segments' parameter determines the "discretization" of the Bezier
 * curve and is the number of straight line segments that should be used
 * to approximate the curve.
 *
 * Call evaluate_bezier_curve() to compute the necessary points on
 * the curve.
 */

void draw_bezier_curve(int num_segments, control_point p[], int num_points) {
    glBegin(GL_LINE_STRIP);
    float x, y;
    for(int i = 0; i <= num_segments; i++) {
        evaluate_bezier_curve(&x, &y, p, num_points, i/(float)num_segments);
        glVertex2f(x, y);
    }
    glEnd();
}

/* Find the intersection of a cubic Bezier curve with the line X=x.
   Return 1 if an intersection was found and place the corresponding y
   value in *y.
   Return 0 if no intersection exists.
*/

float threshold = 0.0001;
int intersect_cubic_bezier_curve(float *y, control_point p[], float x) {
    // If x does not lie in the range of this curve, no intersection exists.
    if (x < p[0].x || x > p[3].x)
        return 0;
    
    float cur_x, u = 0.5, i = 2;
    // Do a binary search for a close enough u
    do {
        // Calculate x for this value of u
        evaluate_bezier_curve(&cur_x, y, p, 4, u);
        // Increase u if the x is too small, decrease it otherwise
        if (cur_x < x) {
            u += 1/pow(2, i);
        } else {
            u -= 1/pow(2, i);
        }
        i++;
    } while (fabs(cur_x - x) > threshold);

    return 1;
}

float B(int i, int n, float u) {
    return bin_dis(n, i) * pow(u, i) * pow((1 - u), n - i);
}

float bin_dis(int n, int k) {
    return fact(n)/(float)(fact(k) * fact(n - k));
}

int fact(int q) {
    if (q <= 0) {
        return 1;
    } else {
        int res = q;
        for(int i = q - 1; i > 0; i--) {
            res *= i;
        }
        return res;
    }
}
