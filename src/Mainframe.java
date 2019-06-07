import processing.core.PApplet;
import processing.opengl.PShader;
import themidibus.MidiBus;

public class Mainframe extends PApplet {

    private MidiBus midiInterface;
    private PShader myShader;
    private float timeCount;

    public void settings() {
        size(1280, 720, P2D);
    }

    public void setup() {
        frameRate(60);
    }

}
