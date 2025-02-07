/* Computer Graphics, Assignment, Translations, Rotations and Scaling
 *
 * Filename ........ transformations.c
 * Description ..... Contains the re-programmed translation, rotation and scaling functions
 * Student name .... Bas Visser / Nardi Lam
 * Student email ... bas.visser2@student.uva.nl / mij@nardilam.nl
 * Collegekaart .... 10439013 / 10453555
 * Date ............ 2014/02/14
 * Comments ........
 *
 *
 * (always fill in these fields before submitting!!)
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <GL/gl.h>
#include "transformations.h"

/* ANSI C/ISO C89 does not specify this constant (?) */
#ifndef M_PI
#define M_PI           3.14159265358979323846  /* pi */
#endif

void myScalef(GLfloat x, GLfloat y, GLfloat z)
{
    GLfloat M[16] =
    {
        x,   0.0, 0.0, 0.0,
        0.0, y,   0.0, 0.0,
        0.0, 0.0, z,   0.0,
        0.0, 0.0, 0.0, 1.0
    };

    glMultMatrixf(M);
}


void myTranslatef(GLfloat x, GLfloat y, GLfloat z)
{
    GLfloat M[16] =
    {
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        x,   y,   z,   1.0
    };

    glMultMatrixf(M);
}

void myRotatef(GLfloat angle, GLfloat x, GLfloat y, GLfloat z)
{
    GLfloat u[3], v[3], w[3], t[3];

    //
    // 1. Create the orthonormal basis
    //

    // Store the incoming rotation axis in w and normalize w
    GLfloat wlen = sqrt(x*x + y*y + z*z);
    w[0] = x/wlen; w[1] = y/wlen; w[2] = z/wlen;

    // Compute the value of t, based on w
    t[0] = w[0]; t[1] = w[1]; t[2] = w[2];
    int min;
    if (x < y && x < z) {
        min = 0;
    } else if (y < x && y < z) {
        min = 1;
    } else {
        min = 2;
    }
    t[min] = 1;

    // Compute u = t x w
	u[0] = (t[1] * w[2]) - (t[2] * w[1]);
	u[1] = (t[2] * w[0]) - (t[0] * w[2]);
	u[2] = (t[0] * w[1]) - (t[1] * w[0]);
    // Normalize u
	GLfloat ulen = sqrt(u[0]*u[0] + u[1]*u[1] + u[2]*u[2]);
	u[0] = u[0]/ulen; u[1] = u[1]/ulen; u[2] = u[2]/ulen;

    // Compute v = w x u
	v[0] = (w[1] * u[2]) - (w[2] * u[1]);
	v[1] = (w[2] * u[0]) - (w[0] * u[2]);
	v[2] = (w[0] * u[1]) - (w[1] * u[0]);
	
	
    // At this point u, v and w should form an orthonormal basis.
    // If your routine does not seem to work correctly it might be
    // a good idea to the check the vector values.

    //
    // 2. Set up the three matrices making up the rotation
    //

    // Specify matrix A

    GLfloat A[16] =
    {
        0[u], u[1], u[2], 0.0,
        v[0], v[1], v[2], 0.0,
        w[0], w[1], w[2], 0.0,
        0.0, 0.0, 0.0, 1.0
    };

    // Convert 'angle' to radians
	angle = angle * (3.14159265358979323846/180);
    // Specify matrix B

    GLfloat B[16] =
    {
        cos(angle), sin(angle), 0.0, 0.0,
        -sin(angle),  cos(angle), 0.0, 0.0,
        0.0,         0.0,        1.0, 0.0,
        0.0,         0.0,        0.0, 1.0
    };

    // Specify matrix C

    GLfloat C[16] =
    {
        u[0], v[0], w[0], 0.0,
        u[1], v[1], w[1], 0.0,
        u[2], v[2], w[2], 0.0,
        0.0, 0.0, 0.0, 1.0
    };

    //
    // 3. Apply the matrices to get the combined rotation
    //

    glMultMatrixf(A);
    glMultMatrixf(B);
    glMultMatrixf(C);
}

