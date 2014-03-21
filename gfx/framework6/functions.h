void create_custom_poly(point_t p, int vert_count);
bool is_concave_poly();
bool are_vertices_concave(b2Vec2 tmp1, b2Vec2 tmp2,bool *got_sign, bool *positive);