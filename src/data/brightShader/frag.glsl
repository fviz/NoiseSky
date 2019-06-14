#version 150

uniform float time;
uniform vec2 resolution;

uniform float XFOVDistortionInput;
uniform float weirdMirroringInput;
uniform float amountInput;
uniform float clippingInput;
uniform float noise1Input;
uniform float noise2Input;
uniform float sizeInput;
uniform float speed;
uniform bool toggleFill;

in VertexData
{
    vec4 v_position;
    vec3 v_normal;
    vec2 v_texcoord;
} inData;

out vec4 fragColor;

float XFOV = 1.5707 + sin(time) * XFOVDistortionInput;

float de(vec3 p){
    float clipping = clippingInput;// Default 4.0
    vec3 s=vec3(clipping, clipping, clipping);// NOISY APPEARENCE

    float noise1 = noise1Input;// Default 1
    float noise2 = noise2Input;// Default 8.0
    float size = sizeInput;// Default 1.0

    return length(s-mod(p*noise1, noise2))-size;// Last value is size
}

void main(void) {
    float t = time;
    vec2 viewDir=vec2(0.0, 0.0);
    //    viewDir = vec2(0.0 + (time/10), 0.0 + (time/10));                  // Rotate
    vec3 pos=vec3(t*2, cos(t)*0.0, 0.0);// Camera movement

    vec2 rayDir=(gl_FragCoord.xy-resolution/2.0)/resolution;//x and y from -0.5 to 0.5, weird camera settings. Default 2.0
    rayDir=rayDir*vec2(XFOV, XFOV*(resolution.y/resolution.x))+viewDir;
    //    rayDir=rayDir*vec2(XFOV+sin(time),XFOV*(resolution.y/resolution.x) + sin(time))+viewDir;

    vec3 unitRay=vec3(cos(rayDir.x), sin(rayDir.x), sin(rayDir.y));
    float amount = amountInput;     // Default: 1
    //    unitRay=unitRay-amount*length(unitRay/amount);
    unitRay=unitRay/length(unitRay/amount);

    vec3 rayPos=pos;
    for (int i=0; i<50; i++){
        //        rayPos=rayPos+unitRay*de(rayPos)-100;                 // Weird mirroring
        rayPos=rayPos+unitRay*de(rayPos)-weirdMirroringInput;// Weird mirroring
        if (toggleFill == true) {
            if (abs(de(rayPos))<0.1||length(pos-rayPos)>100.0){break;}
        }
    }

    float par1 = 1;// Default: 1
    float texturing = 1;// Default: 1
    float dist=par1-de(rayPos)*texturing;
        fragColor = vec4(
        dist*1,
        (sin(1.*rayPos.z)/2.0+0.5)/dist*0.6,
        (sin(rayPos.x/10.0)/2.0+0.5)/dist,
        1.0
        );

//    fragColor = vec4(
//    0+dist/rayPos.z-0.1,
//    0+dist/rayPos.z-0.1,
//    0+dist/rayPos.z-0.1,
//    1.0
//    );

//            fragColor = vec4(
//                rayPos.x*sin(dis t),
//                rayPos.x*sin(dist-20000),
//                rayPos.x*sin(dist-20000),
//                1.0
//            );

}