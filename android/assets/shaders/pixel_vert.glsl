// fixed attributes from renderable
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

// custom data from shader object
uniform mat4 u_worldTrans;      // this gets renderable.transform
uniform mat4 u_projViewTrans;   // this gets cam.combined

// these to pass on to fragment shader
varying vec3 v_normal;

// this is called for each vertex
void main() {
    v_normal = a_normal;
    gl_Position = u_projViewTrans * u_worldTrans * a_position;
}
