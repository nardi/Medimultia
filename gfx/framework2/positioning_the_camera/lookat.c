/* Computer Graphics, Assignment 4, Positioning the camera
 *
 * Filename ........ lookat.c
 * Description ..... Contains the re-programmed lookAt function
 * Created by ...... Jurgen Sturm 
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
#include <GL/glut.h>   
#include <GL/gl.h>
#include <GL/glu.h>
#include <math.h>

 
/* ANSI C/ISO C89 does not specify this constant (?) */
#ifndef M_PI
#define M_PI           3.14159265358979323846  /* pi */
#endif

void myLookAt(GLdouble eyeX, GLdouble eyeY, GLdouble eyeZ,
              GLdouble centerX, GLdouble centerY, GLdouble centerZ,
              GLdouble upX, GLdouble upY, GLdouble upZ) {

    //glTranslated(-eyeX, -eyeY, -eyeZ);
    GLfloat cx[3], cy[3], cz[3], czlen, cxlen;
    cz[0] = centerX - eyeX; cz[1] = centerY - eyeY; cz[2] = centerZ - eyeZ;
    //cz[0] =  eyeX - centerX; cz[1] = eyeY - centerY; cz[2] =  eyeZ - centerZ;

    czlen = sqrt(cz[0]*cz[0] + cz[1]*cz[1] + cz[2]*cz[2]);
    cz[0] /= czlen; cz[1] /= czlen; cz[2] /= czlen;

    cx[0] = (-upY * cz[2]) - (-upZ * cz[1]);
	cx[1] = (-upZ * cz[0]) - (-upX * cz[2]);
	cx[2] = (-upX * cz[1]) - (-upY * cz[0]);
    
    cxlen = sqrt(cx[0]*cx[0] + cx[1]*cx[1] + cx[2]*cx[2]);
    cx[0] /= cxlen; cx[1] /= cxlen; cx[2] /= cxlen;

    cy[0] = (cx[1] * cz[2]) - (cx[2] * cz[1]);
	cy[1] = (cx[2] * cz[0]) - (cx[0] * cz[2]);
	cy[2] = (cx[0] * cz[1]) - (cx[1] * cz[0]);

    /*GLfloat R[16] = {
                        cx[0], cx[1], cx[2], 0.0,
                        cy[0], cy[1], cy[2], 0.0,
                        -cz[0], -cz[1], -cz[2], 0.0,
                        0.0, 0.0, 0.0, 1.0
    };*/

    GLfloat R[16] = {
                        cx[0], cy[0], -cz[0], 0.0,
                        cx[1], cy[1], -cz[1], 0.0,
                        cx[2], cy[2], -cz[2], 0.0,
                        0.0, 0.0, 0.0, 1.0
    };


    glMultMatrixf(R);   
    glTranslated(-eyeX, -eyeY, -eyeZ);
}
