/* Computer Graphics and Game Technology, Assignment Ray-tracing
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
#include <stdio.h>
#include <stdlib.h>
#include "shaders.h"
#include "perlin.h"
#include "v3math.h"
#include "intersection.h"
#include "scene.h"
#include "quat.h"
#include "constants.h"

// shade_constant()
//
// Always return the same color. This shader does no real computations
// based on normal, light position, etc. As such, it merely creates
// a "silhouette" of an object.

vec3
shade_constant(intersection_point ip)
{
    return v3_create(1, 0, 0);
}

vec3
shade_matte(intersection_point ip)
{
    float l_here, l_full;
    
    l_here = l_full = scene_ambient_light;
    
    for (int i = 0; i < scene_num_lights; i++)
    {
        l_full += scene_lights[i].intensity;
    
        vec3 li = v3_normalize(v3_subtract(scene_lights[i].position, ip.p));
        
        if (!shadow_check(v3_add(ip.p, v3_multiply(li, 0.0001)), li))
            l_here += scene_lights[i].intensity * fmax(0, v3_dotprod(ip.n, li));
    }
    
    return v3_multiply(v3_create(1, 1, 1), l_here / l_full);
}

vec3
shade_blinn_phong(intersection_point ip)
{
    float kd = 0.8, ks = 0.5, a = 50, diffuse = 0, specular = 0;
    vec3 cd = v3_create(1,0,0), cs = v3_create(1,1,1);
    
    for (int i = 0; i < scene_num_lights; i++)
    {    
        vec3 li = v3_normalize(v3_subtract(scene_lights[i].position, ip.p)),
             h = v3_normalize(v3_add(ip.i, li));
        
        diffuse += scene_lights[i].intensity * fmax(0, v3_dotprod(ip.n, li));
        specular += scene_lights[i].intensity * pow(v3_dotprod(ip.n, h), a);
    }
    
    return v3_add(v3_multiply(cd, scene_ambient_light + kd * diffuse),
                  v3_multiply(cs, ks * specular));
}

vec3
shade_reflection(intersection_point ip)
{
    vec3 matte = shade_matte(ip),
         r = v3_subtract(v3_multiply(ip.n, 2 * v3_dotprod(ip.i, ip.n)), ip.i),
         p_offset = v3_add(ip.p, v3_multiply(r, 0.0001)),
         reflect = ray_color(ip.ray_level + 1, p_offset, r);
         
    return v3_add(v3_multiply(matte, 0.75), v3_multiply(reflect, 0.25));
}

// Returns the shaded color for the given point to shade.
// Calls the relevant shading function based on the material index.
vec3
shade(intersection_point ip)
{
  switch (ip.material)
  {
    case 0:
      return shade_constant(ip);
    case 1:
      return shade_matte(ip);
    case 2:
      return shade_blinn_phong(ip);
    case 3:
      return shade_reflection(ip);
    default:
      return shade_constant(ip);

  }
}

// Determine the surface color for the first object intersected by
// the given ray, or return the scene background color when no
// intersection is found
vec3
ray_color(int level, vec3 ray_origin, vec3 ray_direction)
{
    intersection_point  ip;

    // If this ray has been reflected too many times, simply
    // return the background color.
    if (level >= 3)
        return scene_background_color;

    // Check if the ray intersects anything in the scene
    if (find_first_intersection(&ip, ray_origin, ray_direction))
    {
        // Shade the found intersection point
        ip.ray_level = level;
        return shade(ip);
    }

    // Nothing was hit, return background color
    return scene_background_color;
}
