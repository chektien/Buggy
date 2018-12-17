/*
 * Graphics: shaders
 * 1. Shader program structure overview.
 */

// fixed standard data from renderable
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

// custom data from shader object
uniform mat4 u_worldTrans;      // this gets renderable.transform
uniform mat4 u_projViewTrans;   // this gets cam.combined

// these to pass on to fragment shader
varying vec4 v_color;
varying vec2 v_texCoords;

// this is called for each vertex
void main() {
    // relay data to the fragment shader
    v_color = a_color;
    v_texCoords = a_texCoord0;

    // this will be the position fed into the interpolator in the pipeline
    gl_Position = u_projViewTrans * u_worldTrans * a_position;
}