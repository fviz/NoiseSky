import processing.core.PApplet;
import processing.opengl.PShader;
import themidibus.MidiBus;
import oscP5.*;

import javax.sound.midi.MidiMessage;

public class NoiseSky extends PApplet {

    private MidiBus midiInterface;
    private PShader myShader;
    private float timeCount;
    private OscP5 osc;

    private float XFOVDistortion = 1;
    private float weirdMirroring = 1;
    private float amount = 1;
    private float clipping;
    private float noise1 = 1;
    private float noise2 = 8;
    private float size = 1;

    public void settings() {
        size(1280, 720, P2D);
    }

    public void setup() {
        frameRate(60);
        myShader = loadShader("frag.glsl", "vert.glsl");
        midiInterface = new MidiBus(this);
        midiInterface.addInput(1);

        osc = new OscP5(this,12000);

    }

    public void draw() {
        timeCount += 0.01;
        sets();
        shader(myShader);

        beginShape();
        vertex(0, 0);
        vertex(width, 0);
        vertex(width, height);
        vertex(0, height);
        endShape();

    }

    public void midiMessage(MidiMessage message) { // You can also use midiMessage(MidiMessage message, long timestamp, String bus_name)
        // Receive a MidiMessage
        // MidiMessage is an abstract class, the actual passed object will be either javax.sound.midi.MetaMessage, javax.sound.midi.ShortMessage, javax.sound.midi.SysexMessage.
        // Check it out here http://java.sun.com/j2se/1.5.0/docs/api/javax/sound/midi/package-summary.html
//        println();
//        println("MidiMessage Data:");
//        println("--------");
//        println("Status Byte/MIDI Command:"+message.getStatus());
//        for (int i = 1;i < message.getMessage().length;i++) {
//            println("Param "+(i+1)+": "+(int)(message.getMessage()[i] & 0xFF));
//        }

        int channel = message.getMessage()[1];
        float value = message.getMessage()[2];

        if (channel == 48) {
            XFOVDistortion = (float) (value);
            println("XFOVDistortion: " + XFOVDistortion);
        }

        if (channel == 49) {
            weirdMirroring = map(value, 0, 127, -2, 2);
            println("weirdMirroring: " + weirdMirroring);
        }

        if (channel == 50) {
            amount = map(value, 0, 127, 0, 10);
            println("amount: " + amount);
        }

        if (channel == 51) {
            clipping = map(value, 0, 127, 0, 20);
            println("clipping: " + clipping);
        }

        if (channel == 52) {
            noise1 = map(value, 0, 127, 0, 20);
            println("noise1: " + noise1);
        }

        if (channel == 53) {
            noise2 = map(value, 0, 127, 0, 20);
            println("noise2: " + noise2);
        }

        if (channel == 54) {
            size = map(value, 0, 127, 0, 20);
            println("size: " + size);
        }

        if (channel == 32 && value == 0) {
            weirdMirroring = 0;
        }
    }

    /* incoming osc message are forwarded to the oscEvent method. */
    void oscEvent(OscMessage oscMessageInput) {
        /* print the address pattern and the typetag of the received OscMessage */
        print("### received an osc message.");
        print(" addrpattern: "+oscMessageInput.addrPattern());
        println(" typetag: "+oscMessageInput.typetag());

        if (oscMessageInput.addrPattern().equals("/main/tune")) {
            float value = oscMessageInput.get(0).floatValue();
            noise1 = 1 + noise1 * value;
            noise2 = 8 + value * -2;
            size = 1 + value * 5;
        }
    }

    public void sets() {
        myShader.set("time", timeCount);
        myShader.set("XFOVDistortionInput", XFOVDistortion);
        myShader.set("weirdMirroringInput", weirdMirroring);
        myShader.set("amountInput", amount);
        myShader.set("clippingInput", clipping);
        myShader.set("noise1Input", noise1);
        myShader.set("noise2Input", noise2);
        myShader.set("sizeInput", size);
    }

    public static void main(String[] args) {
        PApplet.main("NoiseSky");
    }

}
