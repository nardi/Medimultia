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
#include "functions.h"

unsigned int reso_x = 800, reso_y = 600; // Window size in pixels
const float world_x = 8.f, world_y = 6.f; // Level (world) size in meters
float px_to_m = reso_x/world_x;
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

float GRAVITY = -9.81f;
bool PLAY = false;
//Custom polygon
int custom_limit = 4;
b2Vec2 *custom_vertices = NULL;
int point_index = 0;
bool mouse_released = false;

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
    world = new b2World(b2Vec2(0.0f, GRAVITY));   
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
    struct timespec sleep = {0};
    sleep.tv_nsec = 10000000;
    nanosleep(&sleep, NULL);

    if(PLAY){
        world->Step(1/60.0f, 8, 3);
        world->ClearForces();
    }

    b2Body *body = world->GetBodyList();
    while (body != NULL)
    {
        b2Vec2 pos = body->GetPosition();
        b2Fixture *fixture = body->GetFixtureList();
        while (fixture != NULL)
        {
            b2Shape *shape = fixture->GetShape();
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
    
    point_t end = levels[cur_level].end;
    float size = 0.36;
    glColor3f(1.0, 1.0, 0.9);
    glBegin(GL_POLYGON);
        glVertex2f(end.x + 0.25f*size, end.y + 0.5f*size);
        glVertex2f(end.x + 0.5f*size, end.y + 0.25f*size);
        glVertex2f(end.x - 0.25f*size, end.y - 0.5f*size);
        glVertex2f(end.x - 0.5f*size, end.y - 0.25f*size);
    glEnd();
    glBegin(GL_POLYGON);
        glVertex2f(end.x - 0.25f*size, end.y + 0.5f*size);
        glVertex2f(end.x - 0.5f*size, end.y + 0.25f*size);
        glVertex2f(end.x + 0.25f*size, end.y - 0.5f*size);
        glVertex2f(end.x + 0.5f*size, end.y - 0.25f*size);
    glEnd();
    
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
        case 'g':
            PLAY = !PLAY;
            printf("Youpressedg\n");
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
    if(mouse_released){
        point_t p;
        /*Save each click in Box2D coordinates (100pixels=1metre) and y-axis inverted*/
        p.x = x/px_to_m;
        p.y = -(y/px_to_m)+world_y;
        //DEBUG
        printf("Point %d with coordinates %g, %g has been saved!\n", point_index, p.x, p.y);
        create_custom_poly(p, point_index);
    }
    /*This function is called twice for each mouseclick; once on mouse down, once on mouse up. This variable ensures that only one coordinate
    is saved for each click*/
    mouse_released = !mouse_released;
}

void create_custom_poly(point_t poly_point, int vert_count){
    bool done = false;
	if(custom_limit > 8){
		printf("Custom polygons cannot have more than 8 vertices\n");
		custom_limit = 8;
	}

    if(custom_vertices == NULL){
        custom_vertices = new b2Vec2[custom_limit];
    }

    if(vert_count < custom_limit){
        //DEBUG
        printf("Setting index %d as %g, %g\n", vert_count, poly_point.x, poly_point.y);
        custom_vertices[vert_count].Set(poly_point.x, poly_point.y);
        point_index++;
        printf("Set index %d as %g,%g\n", vert_count, custom_vertices[vert_count].x, custom_vertices[vert_count].y);
        if(vert_count == custom_limit - 1){
            done = true;
        }
    }

    if(done){
        /*if(is_concave_poly()){
            fix_poly();
        }*/
        b2PolygonShape custom_poly;

        custom_poly.Set(custom_vertices, custom_limit);
        
        b2BodyDef custom_body_def;
        custom_body_def.type = b2_dynamicBody;
        //DEBUG
        custom_body_def.position.Set(custom_vertices[0].x, custom_vertices[0].y);

        b2Body *custom_body = world->CreateBody(&custom_body_def);

        b2FixtureDef custom_fixture_def;
        custom_fixture_def.shape = &custom_poly;
        custom_fixture_def.density = 1.0;
        custom_fixture_def.friction = 1.0;
        custom_body->CreateFixture(&custom_fixture_def);

        delete custom_vertices;
        custom_vertices = NULL;
        point_index = 0;
    }
    
}



bool is_concave_poly(){
    b2Vec2 *flawed_poly = custom_vertices;
    int vert_count = custom_limit - 1;
    bool positive, got_sign = false;
    for(int i = 0; i < vert_count - 1; i++){
        b2Vec2 tmp1 = flawed_poly[i];
        b2Vec2 tmp2 = flawed_poly[i+1];
        if(are_vertices_concave(tmp1, tmp2, &positive, &got_sign)){
            return true;
        }        
    }
    b2Vec2 tmp1 = flawed_poly[vert_count - 1];
    b2Vec2 tmp2 = flawed_poly[0];

    if(are_vertices_concave(tmp1, tmp2, &positive, &got_sign)){
            return true;
    }

    return false;

}

bool are_vertices_concave(b2Vec2 tmp1, b2Vec2 tmp2,bool *got_sign, bool *positive){
    if(!*got_sign){
            if(!b2Cross(tmp1, tmp2) < 0){
                *positive = true;
                *got_sign = true;
            }
            else{
                *positive = false;
                *got_sign = true;
            }
    }
    else{
        if(b2Cross(tmp1, tmp2) < 0 && *positive){
            return true;
        }
        if(!(b2Cross(tmp1, tmp2) < 0) && !*positive){
            return true;
        }
    }
    return false;
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
