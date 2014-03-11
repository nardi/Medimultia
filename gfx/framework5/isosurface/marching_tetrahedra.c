/* Computer Graphics, Assignment, Volume rendering with cubes/points/isosurface
 *
 * Student name ....
 * Student email ...
 * Collegekaart ....
 * Date ............
 * Comments ........
 *
 * (always fill in these fields before submitting!!)
 */

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "marching_tetrahedra.h"

/* Compute a linearly interpolated position where an isosurface cuts
   an edge between two vertices (p1 and p2), each with their own
   scalar value (v1 and v2) */

static vec3
interpolate_points(unsigned char isovalue, vec3 p1, vec3 p2, unsigned char v1, unsigned char v2)
{
    float f1 = fabs((isovalue - v1) / (float)(v2 - v1));
    return v3_add(v3_multiply(p1, f1), v3_multiply(p2, 1 - f1));
}

/* Using the given iso-value generate triangles for the tetrahedron
   defined by corner vertices v0, v1, v2, v3 of cell c.

   Store the resulting triangles in the "triangles" array.

   Return the number of triangles created (either 0, 1, or 2).

   Note: the output array "triangles" should have space for at least
         2 triangles.
*/

static int
generate_tetrahedron_triangles(triangle *triangles, unsigned char isovalue, cell c, int v0, int v1, int v2, int v3)
{
    int num_t = 0;
    int v[] = { v0, v1, v2, v3 };
    unsigned char B = 0;
    for (int i = 0; i < 4; i++)
    {
        if (c.value[v[i]] > isovalue)
            B |= (1 << i);
    }
    if (B & (1 << 3)) B ^= 15; // So we only have 7 cases, those where the first bit is a 0
    
#define interpolate(a, b) interpolate_points(isovalue, c.p[v[a]], c.p[v[b]], \
    c.value[v[a]], c.value[v[b]])
    
    triangle t1, t2;
    switch (B)
    {
        case 1:
            t1.p[0] = interpolate(0, 1);
            t1.p[1] = interpolate(0, 2);
            t1.p[2] = interpolate(0, 3);
            triangles[0] = t1; num_t = 1;
        break;
        case 2:
            t1.p[0] = interpolate(1, 0);
            t1.p[1] = interpolate(1, 2);
            t1.p[2] = interpolate(1, 3);
            triangles[0] = t1; num_t = 1;
        break;
        case 3:
            t1.p[0] = interpolate(0, 2);
            t1.p[1] = interpolate(2, 1);
            t1.p[2] = interpolate(1, 3);
            t2.p[0] = interpolate(1, 3);
            t2.p[1] = interpolate(3, 0);
            t2.p[2] = interpolate(0, 2);
            triangles[0] = t1; triangles[1] = t2; num_t = 2;
        break;
        case 4:
            t1.p[0] = interpolate(2, 0);
            t1.p[1] = interpolate(2, 1);
            t1.p[2] = interpolate(2, 3);
            triangles[0] = t1; num_t = 1;
        break;
        case 5:
            t1.p[0] = interpolate(0, 1);
            t1.p[1] = interpolate(1, 2);
            t1.p[2] = interpolate(2, 3);
            t2.p[0] = interpolate(2, 3);
            t2.p[1] = interpolate(3, 0);
            t2.p[2] = interpolate(0, 1);
            triangles[0] = t1; triangles[1] = t2; num_t = 2;
        break;
        case 6:
            t1.p[0] = interpolate(2, 0);
            t1.p[1] = interpolate(0, 1);
            t1.p[2] = interpolate(1, 3);
            t2.p[0] = interpolate(1, 3);
            t2.p[1] = interpolate(3, 2);
            t2.p[2] = interpolate(2, 0);
            triangles[0] = t1; triangles[1] = t2; num_t = 2;
        break;
        case 7:
            t1.p[0] = interpolate(3, 0);
            t1.p[1] = interpolate(3, 1);
            t1.p[2] = interpolate(3, 2);
            triangles[0] = t1; num_t = 1;
        break;
    }
    
    return num_t;
}

/* Generate triangles for a single cell for the given iso-value. This function
   should produce at most 6 * 2 triangles (for which the "triangles" array should
   have enough space).

   Use calls to generate_tetrahedron_triangles().

   Return the number of triangles produced
*/

/* Macro that accepts a single argument as tetrahedon. */
#define gen_th(tri, iso, c, th) generate_tetrahedron_triangles(tri, iso, c, \
        th[0], th[1], th[2], th[3])

int
generate_cell_triangles(triangle *triangles, cell c, unsigned char isovalue)
{
    /* Generates triangles for the following tetrahedons:
         T1: 0, 1, 3, 7
         T3: 0, 1, 5, 7
         T2: 0, 2, 6, 7
         T4: 0, 2, 3, 7
         T5: 0, 4, 5, 7
         T6: 0, 4, 6, 7
       These correspond to those in figure 9 in the assignment.
    */
    int toff = 0;
    int th[4];
    th[0] = 0; th[3] = 7;
    
    /* T1, T3 */
    th[1] = 1;
    th[2] = 1 + 2;
    toff += gen_th(triangles + toff, isovalue, c, th);
    th[2] = 1 + 4;
    toff += gen_th(triangles + toff, isovalue, c, th);
    
    /* T2, T4 */
    th[1] = 2;
    th[2] = 2 + 1;
    toff += gen_th(triangles + toff, isovalue, c, th);
    th[2] = 2 + 4;
    toff += gen_th(triangles + toff, isovalue, c, th);
    
    /* T5, T6 */
    th[1] = 4;
    th[2] = 4 + 1;
    toff += gen_th(triangles + toff, isovalue, c, th);
    th[2] = 4 + 2;
    toff += gen_th(triangles + toff, isovalue, c, th);

    return toff;
}
