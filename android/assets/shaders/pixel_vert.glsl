// fixed attributes from renderable
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

// custom data from shader object
uniform mat4 u_worldTrans;      // this gets renderable.transform
uniform mat4 u_projViewTrans;   // this gets cam.combined

// these to pass on to fragment shader
varying vec4 v_color;
varying vec2 v_texCoord0;

// this is called for each vertex
void main() {
    v_color = a_color;
    v_texCoord0 = a_texCoord0;
    gl_Position = u_projViewTrans * u_worldTrans * a_position;
}
