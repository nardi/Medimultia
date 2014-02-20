/* Computer Graphics, Assignment, Bezier curves
 * Filename ........ bezier.c
 * Description ..... Bezier curves
 * Date ............ 22.07.2009
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

#include <math.h>
#include "bezier.h"
#include <stdio.h>

/* Given a Bezier curve defined by the 'num_points' control points
 * in 'p' compute the position of the point on the curve for parameter
 * value 'u'.
 *
 * Return the x and y values of the point by setting *x and *y,
 * respectively.
 */

void
evaluate_bezier_curve(float *x, float *y, control_point p[], int num_points, float u)
{
    printf("U: %g\n", u);
    float b;
    *x = 0; *y = 0;
    for(int i = 0; i < num_points; i++){
        b = B(i, num_points - 1, u);
        printf("B: %g\n" , b);
        *x += b * p[i].x;
        *y += b * p[i].y;        
    }
    
    //*x = 0.0;
    //*y = 0.0;
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

void
draw_bezier_curve(int num_segments, control_point p[], int num_points)
{
//    printf("Hello\n");
    glBegin(GL_LINE_STRIP);
    float x,y;
    for(int i = 0; i <= num_segments; i++){
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

int
intersect_cubic_bezier_curve(float *y, control_point p[], float x)
{
    return 0;
}

float B(int i, int n, float u){
    return bin_dis(n, i) * pow(u, i) * pow((1 - u), n - i);
}

float bin_dis(int n, int k){
    return fact(n)/(float)(fact(k) * fact(n - k));
}

int fact(int q){
    printf("Q: %d\n" , q);
    int res = q;
    if(q <= 0){
        return 1;
    }
    else{
        for(int i = q-1; i > 0; i--){
            res *= i;
        }
        printf("Res: %d\n", res);
        return res;
    }
}
