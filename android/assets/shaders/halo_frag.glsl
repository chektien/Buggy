#ifdef GL_ES
    precision mediump float;
#endif

#define PI 3.1415926535897932384626433832795

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int u_hasTexture;
uniform float u_time;
uniform vec4 u_mouse;

void main() {
    vec2 iResolution = vec2(720, 1280);

    vec2 uv = 1.5*(2.0*v_texCoords.xy - iResolution.xy) / iResolution.y;
    vec2 mouse = 1.5*(2.0*u_mouse.xy - iResolution.xy) / iResolution.y;
    vec2 offset = vec2(cos(u_time/2.0)*mouse.x,sin(u_time/2.0)*mouse.y);;

    vec3 light_color = vec3(0.9, 0.65, 0.5);
    float light = 0.0;

    light = 0.1 / distance(normalize(uv), uv);

    if(length(uv) < 1.0){
        light *= 0.1 / distance(normalize(uv-offset), uv-offset);
    }


    vec4 texColor = texture2D(u_texture, v_texCoords).rgba;
    if (u_hasTexture==0) {
        texColor = v_color;
    }

    //gl_FragColor = funkyColor*0.65 + texColor*0.35;



    gl_FragColor = vec4(light*light_color, 1.0);
}