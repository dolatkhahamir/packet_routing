package canvas;

public class DynamicCanvasPanel extends AbstractCanvasPanel {
    protected int realFps;

    public DynamicCanvasPanel(boolean start) {
        super();
        ticksCounter = 0;
        loopCounter = 0;
        realFps = fps;
        if (start)
            start();
    }

    public DynamicCanvasPanel() {
        this(false);
    }

    public int getRealFps() {
        return realFps;
    }

    @Override
    public void run() {
        long now;
        long lastTime = System.nanoTime();
        final double timePerTick = 1_000_000_000 / (double) fps;
        double delta = 0;
        long timer = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                firstTicks.forEach(Runnable::run);
                renderManager.tick();
                delta--;
                ticksCounter++;
                repaint();
            }
            loopCounter++;

            if (timer > 1_000_000_000) {
                timer = 0;
                ticksCounter = 0;
                loopCounter = 0;
            }
        }
        running = false;
    }
}
