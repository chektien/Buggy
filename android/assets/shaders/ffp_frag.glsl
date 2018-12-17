#ifdef GL_ES
    precision mediump float;
#endif

// fixed standard data from the pipeline
varying vec4 v_color;
varying vec2 v_texCoords;

// custom data from the app
uniform sampler2D u_texture;
uniform int u_hasTexture;
uniform float u_time; // todo: make shader management more elegant in java

// this is called for each fragment (i.e., pre-pixel)
void main() {
    u_time;

    // set each frag's color to the sampled texture's color (if available)
    if (u_hasTexture == 1) {
        gl_FragColor = texture2D(u_texture, v_texCoords).rgba;
    }

    // take the static color if no texture
    else {
        gl_FragColor = v_color;
    }
}
