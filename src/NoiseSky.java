import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PShader;
import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;
import oscP5.*;
import javax.sound.midi.MidiMessage;
public class NoiseSky extends PApplet {

    private MidiBus midiInterface;
    private PShader mainShader;
    private PShader brightShader;
    private PShader currentShader;
    private PShader[] shaders;
    private float timeCount;
    private OscP5 osc;

    static float XFOVDistortion = 127;
    static float weirdMirroring = (float) 2;
    static float amount = (float) 1.5;
    static float clipping = (float) 12;
    static float noise1 = (float) 20;
    static float noise2 = (float) 20;
    static float size = 20;

    public void settings() {
        size(1280, 720, P3D);
//        fullScreen();
    }
    public void setup() {
        osc = new OscP5(this,12000);

        frameRate(60);
        mainShader = loadShader("mainShader/frag.glsl", "mainShader/vert.glsl");
        brightShader = loadShader("brightShader/frag.glsl", "brightShader/vert.glsl");
        currentShader = mainShader;
        midiInterface = new MidiBus(this);
        midiInterface.addInput(1);


    }

    public void draw() {
        Functions.run();
        timeCount += 0.01;
        sets();
        shader(currentShader);

        beginShape();
        vertex(0, 0);
        vertex(width, 0);
        vertex(width, height);
        vertex(0, height);
        endShape();
        resetShader();

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
            Functions.XFOVDistortionTarget = value;
            println("XFOVDistortion: " + XFOVDistortion);
        }

        if (channel == 49) {
            Functions.weirdMirroringTarget = value;
            println("weirdMirroring: " + weirdMirroring);
        }

        if (channel == 50) {
            Functions.amountTarget = value;
            println("amount: " + amount);
        }

        if (channel == 51) {
            Functions.clippingTarget = value;
            println("clipping: " + clipping);
        }

        if (channel == 52) {
            Functions.noise1Target = value;
            println("noise1: " + noise1);
        }

        if (channel == 53) {
            Functions.noise2Target = value;
            println("noise2: " + noise2);
        }

        if (channel == 54) {
            Functions.sizeTarget = value;
            println("size: " + size);
        }

        if (channel == 55) {
            Functions.easingTarget = map(value, 0, 127, (float) 0.01, (float) 0.1);
            println("easing: " + Functions.easing);
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
            noise2 = 8 + value * (float) -1.2;
            size = 1 + value * -2;
            XFOVDistortion = 1 + value * 5;
            if (value > 0.1) {
                currentShader = brightShader;
                println("BRIGHT");
            } else {
                currentShader = mainShader;
            }
        }
    }

    public void noteOn(Note note) {
        // Receive a noteOn
//        println();
//        println("Note On:");
//        println("--------");
//        println("Channel:"+note.channel());
//        println("Pitch:"+note.pitch());
//        println("Velocity:"+note.velocity());

        // Main
        if (note.pitch == 39) {
            XFOVDistortion = 0;
            weirdMirroring = 0;
            amount = 4;
            clipping = 10;
            noise1 = (float) 0.7;
            noise2 = 20;
            size = 7;
        }
        if (note.pitch == 38) {
            Functions.XFOVDistortionTarget = 0;
            Functions.weirdMirroringTarget = 63;
            Functions.amountTarget = 56;
            Functions.clippingTarget = 63;
            Functions.noise1Target = (float) 4;
            Functions.noise2Target = 127;
            Functions.sizeTarget = 52;
        }

        // Cross
        if (note.pitch == 31) {
            XFOVDistortion = 2;
            weirdMirroring = (float) 0.17322;
            amount = 2;
            clipping = (float) 5.66;
            noise1 = (float) 10;
            noise2 = (float) 14.322;
            size = 7;
        }
        if (note.pitch == 30) {
            Functions.XFOVDistortionTarget = 5;
            Functions.weirdMirroringTarget = 63;
            Functions.amountTarget = 25;
            Functions.clippingTarget = (float) 60;
            Functions.noise1Target = (float) 63;
            Functions.noise2Target = (float) 89;
            Functions.sizeTarget = 48;
        }

        // Lines
        if (note.pitch == 23) {
            XFOVDistortion = 127;
            weirdMirroring = (float) 2;
            amount = (float) 1.5;
            clipping = (float) 12;
            noise1 = (float) 20;
            noise2 = (float) 20;
            size = 20;
        }
        if (note.pitch == 22) {
            Functions.XFOVDistortionTarget = 127;
            Functions.weirdMirroringTarget = 127;
            Functions.amountTarget = 20;
            Functions.clippingTarget = 74;
            Functions.noise1Target = (float) 127;
            Functions.noise2Target = 127;
            Functions.sizeTarget = 127;
        }
    }
    public void noteOff(Note note) {
        // Receive a noteOff
        println();
        println("Note Off:");
        println("--------");
        println("Channel:"+note.channel());
        println("Pitch:"+note.pitch());
        println("Velocity:"+note.velocity());
    }


    // Shader sets
    public void sets() {
        mainShader.set("time", timeCount);
        mainShader.set("XFOVDistortionInput", XFOVDistortion);
        mainShader.set("weirdMirroringInput", weirdMirroring);
        mainShader.set("amountInput", amount);
        mainShader.set("clippingInput", clipping);
        mainShader.set("noise1Input", noise1);
        mainShader.set("noise2Input", noise2);
        mainShader.set("sizeInput", size);
    }

    public static void main(String[] args) {
        PApplet.main("NoiseSky");
    }

}
