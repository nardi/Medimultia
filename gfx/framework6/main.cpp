/* Computer Graphics and Game Technology, Assignment Box2D game
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

#include <cstdio>
#include <time.h>

#include <GL/gl.h>
#include <GL/glut.h>
#include <Box2D/Box2D.h>

#include "levels.h"

unsigned int reso_x = 800, reso_y = 600; // Window size in pixels
const float world_x = 8.f, world_y = 6.f; // Level (world) size in meters

int last_time;
int frame_count;

// Information about the levels loaded from files will be available in these.
unsigned int num_levels;
level_t *levels;

b2World *world = NULL;

/*
 * Load a given world, i.e. read the world from the `levels' data structure and
 * convert it into a Box2D world.
 */
void load_world(unsigned int level)
{
    if (level >= num_levels)
    {
        // Note that level is unsigned but we still use %d so -1 is shown as
        // such.
        printf("Warning: level %d does not exist.\n", level);
        return;
    }

    // Create a Box2D world and populate it with all bodies for this level
    // (including the ball).
    delete world;
    world = new b2World(b2Vec2(0.0f, -9.81f));
    level_t l = levels[level];
    
    b2BodyDef ballDef;
    ballDef.position.Set(0, 0);
    ballDef.type = b2_dynamicBody;
    b2CircleShape circle;
    circle.m_p.Set(0, 0);
    circle.m_radius = 1;
    
    b2Body* ball = world->CreateBody(&ballDef);
    ball->CreateFixture(&circle, 1.0f);
    
    /* for (int i = 0; i < l.num_polygons; i++)
    {
        poly_t poly = l.polygons[i];
        
        b2BodyDef bodyDef;
        bodyDef.position.Set(poly.position.x, poly.position.y);
        bodyDef.type = poly.is_dynamic ? b2_dynamicBody : b2_staticBody;
        b2PolygonShape shape;
        b2Vec2 *points = new b2Vec2[poly.num_verts];
        for (int j = 0; j < poly.num_verts; j++)
        {
            point_t p = poly.verts[j];
            points[j].Set(p.x, p.y);
        }
        shape.Set(points, poly.num_verts);
        
        b2Body* body = world->CreateBody(&bodyDef);
        body->CreateFixture(&shape, 1.0f);
        delete[] points;
    } */
}


/*
 * Called when we should redraw the scene (i.e. every frame).
 * It will show the current framerate in the window title.
 */
void draw(void)
{
    int time = glutGet(GLUT_ELAPSED_TIME);
    int frametime = time - last_time;
    frame_count++;

    // Clear the buffer
    glColor3f(0, 0, 0);
    glClear(GL_COLOR_BUFFER_BIT);


    //
    // Do any logic and drawing here.
    //
    struct timespec sleep = {0};
    sleep.tv_nsec = 20000000;
    nanosleep(&sleep, NULL);

    world->Step(1/60.0f, 8, 3);
    b2Body *ball = world->GetBodyList();
    b2Vec2 pos = ball->GetPosition();
    printf("Ball position: (%g, %g)\n", pos.x, pos.y);

    // Show rendered frame
    glutSwapBuffers();

    // Display fps in window title.
    if (frametime >= 1000)
    {
        char window_title[128];
        snprintf(window_title, 128,
                "Box2D: %f fps, level %d/%d",
                frame_count / (frametime / 1000.f), -1, num_levels);
        glutSetWindowTitle(window_title);
        last_time = time;
        frame_count = 0;
    }
}

/*
 * Called when window is resized. We inform OpenGL about this, and save this
 * for future reference.
 */
void resize_window(int width, int height)
{
    glViewport(0, 0, width, height);
    reso_x = width;
    reso_y = height;
}

/*
 * Called when the user presses a key.
 */
void key_pressed(unsigned char key, int x, int y)
{
    switch (key)
    {
        case 27: // Esc
        case 'q':
            exit(0);
            break;
        // Add any keys you want to use, either for debugging or gameplay.
        default:
            break;
    }
}

/*
 * Called when the user clicked (or released) a mouse buttons inside the window.
 */
void mouse_clicked(int button, int state, int x, int y)
{

}

/*
 * Called when the mouse is moved to a certain given position.
 */
void mouse_moved(int x, int y)
{

}


int main(int argc, char **argv)
{
    // Create an OpenGL context and a GLUT window.
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA);
    glutInitWindowSize(reso_x, reso_y);
    glutCreateWindow("Box2D");

    // Bind all GLUT events do callback function.
    glutDisplayFunc(&draw);
    glutIdleFunc(&draw);
    glutReshapeFunc(&resize_window);
    glutKeyboardFunc(&key_pressed);
    glutMouseFunc(&mouse_clicked);
    glutMotionFunc(&mouse_moved);
    glutPassiveMotionFunc(&mouse_moved);

    // Initialise the matrices so we have an orthogonal world with the same size
    // as the levels, and no other transformations.
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, world_x, 0, world_y, 0, 1);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    // Read the levels into a bunch of structs.
    num_levels = load_levels(&levels);
    printf("Loaded %d levels.\n", num_levels);

    // Load the first level (i.e. create all Box2D stuff).
    load_world(0);

    last_time = glutGet(GLUT_ELAPSED_TIME);
    frame_count = 0;
    glutMainLoop();

    return 0;
}
