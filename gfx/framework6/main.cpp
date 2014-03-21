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
#include <ctime>
#include <map>

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
int cur_level = -1;

b2World *world = NULL;
std::map<b2Shape*, b2Vec3> shape_colors;
b2Body *player_body = NULL;
b2Shape *player_shape = NULL;

b2Vec3 random_color()
{
    b2Vec3 color;
    int bias = rand();
    color.x = 0.35 + 0.25 * rand() / (float)RAND_MAX + 0.2 * ((bias + 0) % 3),
    color.y = 0.35 + 0.25 * rand() / (float)RAND_MAX + 0.2 * ((bias + 1) % 3),
    color.z = 0.35 + 0.25 * rand() / (float)RAND_MAX + 0.2 * ((bias + 2) % 3);
    return color;
}

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
    ballDef.position.Set(l.start.x, l.start.y);
    ballDef.type = b2_dynamicBody;
    b2CircleShape circle;
    circle.m_p.Set(0, 0);
    circle.m_radius = 0.22f;
    
    b2Body *ball = player_body = world->CreateBody(&ballDef);
    b2Fixture *ballFixture = ball->CreateFixture(&circle, 1.0f);
    player_shape = ballFixture->GetShape();
    
    for (unsigned int i = 0; i < l.num_polygons; i++)
    {
        poly_t poly = l.polygons[i];
        
        b2BodyDef bodyDef;
        bodyDef.position.Set(poly.position.x, poly.position.y);
        bodyDef.type = poly.is_dynamic ? b2_dynamicBody : b2_staticBody;
        b2PolygonShape shape;
        b2Vec2 *points = new b2Vec2[poly.num_verts];
        for (unsigned int j = 0; j < poly.num_verts; j++)
        {
            point_t p = poly.verts[j];
            points[j].Set(p.x, p.y);
        }
        shape.Set(points, poly.num_verts);
        
        b2Body *body = world->CreateBody(&bodyDef);
        body->CreateFixture(&shape, 1.0f);
        delete[] points;
    }
    
    cur_level = level;
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
    glColor3f(0.35, 0.35, 0.35);
    glClear(GL_COLOR_BUFFER_BIT);

    //
    // Do any logic and drawing here.
    //
    
    // Sleep to provide a constant fps
    struct timespec sleep = {0};
    sleep.tv_nsec = 10000000;
    nanosleep(&sleep, NULL);

    // Perform physics simulation
    world->Step(1/60.0f, 8, 3);
    
    // Draw all bodies in world
    b2Body *body = world->GetBodyList();
    while (body != NULL)
    {
        b2Vec2 pos = body->GetPosition();
        b2Fixture *fixture = body->GetFixtureList();
        while (fixture != NULL)
        {
            b2Shape *shape = fixture->GetShape();
            // Assign a random color if this shape has none
            if (shape_colors.find(shape) == shape_colors.end())
            {
                shape_colors[shape] = random_color();
            }
            b2Vec3 color = shape_colors[shape];
            glColor3f(color.x, color.y, color.z);
            if (shape->GetType() == 0)
            {
                b2CircleShape *circle = (b2CircleShape*)shape;
                glBegin(GL_TRIANGLE_FAN);
                for (int i = 0; i < 360; i += 5)
                {
                    glVertex2f(pos.x + cos(i * M_PI/180) * circle->m_radius,
                               pos.y + sin(i * M_PI/180) * circle->m_radius);
                }
                glEnd();
            }
            else
            {
                b2PolygonShape *poly = (b2PolygonShape*)shape;
                glBegin(GL_POLYGON);
                for (int i = 0; i < poly->GetVertexCount(); i++)
                {
                    b2Vec2 p = poly->GetVertex(i);
                    glVertex2f(p.x, p.y);
                }
                glEnd();
            }
            fixture = fixture->GetNext();
        }
        body = body->GetNext();
    }
    
    // Draw the endpoint
    point_t end = levels[cur_level].end;
    float size = 0.3, thickness = 0.3;
    glColor3f(1.0, 1.0, 0.9);
    float corner = (1 - thickness) * 0.5f;
    glBegin(GL_POLYGON);
        glVertex2f(end.x + corner*size, end.y + 0.5f*size);
        glVertex2f(end.x + 0.5f*size, end.y + corner*size);
        glVertex2f(end.x - corner*size, end.y - 0.5f*size);
        glVertex2f(end.x - 0.5f*size, end.y - corner*size);
    glEnd();
    glBegin(GL_POLYGON);
        glVertex2f(end.x - corner*size, end.y + 0.5f*size);
        glVertex2f(end.x - 0.5f*size, end.y + corner*size);
        glVertex2f(end.x + corner*size, end.y - 0.5f*size);
        glVertex2f(end.x + 0.5f*size, end.y - corner*size);
    glEnd();
    
    // Detect whether the player has reached the endpoint
    if (player_shape->TestPoint(player_body->GetTransform(), b2Vec2(end.x, end.y)))
    {
        load_world(cur_level + 1);
    }

    // Show rendered frame
    glutSwapBuffers();

    // Display fps in window title.
    if (frametime >= 1000)
    {
        char window_title[128];
        snprintf(window_title, 128,
                "Box2D: %f fps, level %d/%d",
                frame_count / (frametime / 1000.f), cur_level, num_levels);
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
