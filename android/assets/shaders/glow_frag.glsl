#ifdef GL_ES
    precision mediump float;
#endif

#define PI 3.1415926535897932384626433832795

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int u_hasTexture;
uniform float u_time;

void main() {
    vec2 u_k = vec2(8, 8);
    float v = 0.0;
    vec2 c = v_texCoords * u_k - u_k/2.0;
    v += sin((c.x+u_time));
    v += sin((c.y+u_time)/2.0);
    v += sin((c.x+c.y+u_time)/2.0);
    c += u_k/2.0 * vec2(sin(u_time/3.0), cos(u_time/2.0));
    v += sin(sqrt(c.x*c.x+c.y*c.y+1.0)+u_time);
    v = v/2.0;
    vec3 col = vec3(1, sin(PI*v), cos(PI*v));
    vec4 funkyColor = vec4(col*.5 + .5, 1);

    vec4 texColor = texture2D(u_texture, v_texCoords).rgba;
    if (u_hasTexture==0) {
        texColor = v_color;
    }

    gl_FragColor = funkyColor*0.65 + texColor*0.35;
}