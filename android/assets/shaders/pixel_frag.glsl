// these received from vertex shader
varying vec4 v_color;
varying vec2 v_texCoord0;

// this is called for each pixel (fragment)
void main() {
    gl_FragColor = vec4(0,1,1,0.5);
}
