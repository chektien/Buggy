#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int u_hasTexture;
uniform float u_time; // todo: make shader management more elegant in java

// this is called for each pixel (fragment)
void main() {
    u_time;

    if (u_hasTexture == 0) {
        gl_FragColor = v_color;
    }
    else {
        gl_FragColor = texture2D(u_texture, v_texCoords).rgba;
    }
}
