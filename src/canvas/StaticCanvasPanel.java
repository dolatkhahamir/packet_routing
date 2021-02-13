package canvas;

@SuppressWarnings("unused")
public class StaticCanvasPanel extends AbstractCanvasPanel {

    public StaticCanvasPanel() {
        fps = 0;
        running = false;
    }

    public void tickAndRepaint() {
        renderManager.tick();
        repaint();
    }

    @Override
    public void run() {
    }
}
