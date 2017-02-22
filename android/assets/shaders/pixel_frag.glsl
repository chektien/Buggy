precision mediump float;

// these received from vertex shader
#if defined(numDirectionalLights) && (numDirectionalLights > 0)
struct DirectionalLight
{
    vec3 color;
    vec3 direction;
};
uniform DirectionalLight u_dirLights[numDirectionalLights];
#endif

varying vec3 v_normal;

// this is called for each pixel (fragment)
void main() {
    float intensity;
    vec3 lightDir;

    lightDir = vec3(2,2,-3);

#if defined(numDirectionalLights) && (numDirectionalLights > 0)
    intensity = dot(u_dirLights[0].direction, v_normal);
#endif // numDirectionalLights

    intensity = dot(lightDir, v_normal);

    if (intensity > 0.95) {
        gl_FragColor = vec4(1, 0.5, 0.5, 1.0);
    }
    else if (intensity > 0.5) {
        gl_FragColor = vec4(0.6, 0.3, 0.3, 1.0);
    }
    else if (intensity > 0.25) {
        gl_FragColor = vec4(0.4, 0.2, 0.2, 1.0);
    }
    else {
        gl_FragColor = vec4(0.2, 0.1, 0.1, 1.0);
    }
}
