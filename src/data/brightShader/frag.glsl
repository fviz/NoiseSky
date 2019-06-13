#version 150

uniform float time;
uniform vec2 resolution;
uniform vec2 mouse;
uniform vec3 spectrum;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D prevFrame;
uniform sampler2D prevPass;

in VertexData
{
    vec4 v_position;
    vec3 v_normal;
    vec2 v_texcoord;
} inData;

out vec4 fragColor;

#define PI 3.141592
#define TAU 2.*PI

float SDF(vec3 p)
{
    p+=vec3(sin(-time+p.z*9.0)*0.1,0.1*sin(time) * 1,0.0);
    return length(p.xy)-0.4;
}

void main( void ) {

    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = 2.*(gl_FragCoord.xy/resolution.xy)-1.;
    uv.x *= resolution.x/resolution.y;

    vec3 p = vec3 (0.0,0.0,0.0);
    vec3 dir = normalize(vec3(uv*0.5,1.));

    for (int i=0;i<60;i++)
    {
        float d = SDF(p);
        if (abs(d)<0.001)
        {
            break;
        }
        p += d*dir*0.2;
    }

    // Time varying pixel color
    vec3 col = vec3(length(p)*sin(time)*4.1+2, length(p)*sin(time), length(p)*-0.99);

    // Output to screen
    fragColor = vec4(col,1.0);
}