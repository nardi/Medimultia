/* Computer Graphics, Assignment 5, Orthogonal projection
 *
 * Filename ........ ortho.c
 * Description ..... Contains the re-programmed orthogonal projection matrix
 * Date ............ 01.09.2006
 * Created by ...... Jurgen Sturm 
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
#include <GL/glut.h>   
#include <GL/gl.h>
#include <GL/glu.h>
 
#define sqr(x) ((x)*(x))

/* ANSI C/ISO C89 does not specify this constant (?) */
#ifndef M_PI
#define M_PI           3.14159265358979323846  /* pi */
#endif

void myOrtho(GLdouble left,
             GLdouble right,
             GLdouble bottom,
             GLdouble top,
             GLdouble near,
             GLdouble far) {
    GLdouble width = right - left,
             height = top - bottom,
             depth = near - far;
    
    GLdouble P[16] = {
        2/width,  0.0,       0.0,      0.0,
        0.0,      2/height,  0.0,      0.0,
        0.0,      0.0,       2/depth,  0.0,
        -(right + left) / width, -(top+  bottom) / height, -(near + far) / depth, 1.0
    };
    
    glMultMatrixd(P);
}
