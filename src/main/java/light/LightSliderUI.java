package light;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSliderUI;

public class LightSliderUI extends MetalSliderUI {
    public LightSliderUI() {
    }

    public static ComponentUI createUI(JComponent jcomponent) {
        return new LightSliderUI();
    }

    public void paintTrack(Graphics g1) {

    }

    public void paint(Graphics g) {

    }
}
