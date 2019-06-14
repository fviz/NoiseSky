import processing.core.PApplet;

public class Functions {

    static float easingTarget = (float) 0.01;
    static float XFOVDistortionTarget;
    static float weirdMirroringTarget;
    static float amountTarget;
    static float clippingTarget;
    static float noise1Target;
    static float noise2Target;
    static float sizeTarget;


    static float easing = (float) 0.01;

    static void run() {
        easingUpdate();
        XFOVDistortion();
        weirdMirroring();
        amount();
        clipping();
        noise1();
        noise2();
        size();
    }

    private static void easingUpdate() {
        float difference = easingTarget - easing;
        easing += difference * easing;
    }

    private static void XFOVDistortion() {
        float difference = XFOVDistortionTarget - NoiseSky.XFOVDistortion;
        NoiseSky.XFOVDistortion += difference * easing;
    }

    private static void weirdMirroring() {
        float mappedValue = NoiseSky.map(weirdMirroringTarget, 0, 127, -2, 2);
        float difference = mappedValue - NoiseSky.weirdMirroring;
        NoiseSky.weirdMirroring += difference * easing;
    }

    private static void amount() {
        float mappedValue = NoiseSky.map(amountTarget, 0, 127, 0, 10);
        float difference = mappedValue - NoiseSky.amount;
        NoiseSky.amount += difference * easing;
    }

    private static void clipping() {
        float mappedValue = NoiseSky.map(clippingTarget, 0, 127, 0, 20);
        float difference = mappedValue - NoiseSky.clipping;
        NoiseSky.clipping += difference * easing;
    }

    private static void noise1() {
        float mappedValue = NoiseSky.map(noise1Target, 0, 127, 0, 20);
        float difference = mappedValue - NoiseSky.noise1;
        NoiseSky.noise1 += difference * easing;
    }

    private static void noise2() {
        float mappedValue = NoiseSky.map(noise2Target, 0, 127, 0, 20);
        float difference = mappedValue - NoiseSky.noise2;
        NoiseSky.noise2 += difference * easing;
    }

    private static void size() {
        float mappedValue = NoiseSky.map(sizeTarget, 0, 127, 0, 20);
        float difference = mappedValue - NoiseSky.size;
        NoiseSky.size += difference * easing;
    }
}
