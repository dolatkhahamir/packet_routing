package canvas;

import jmath.datatypes.tuples.Point3D;
import jmath.parser.Function4DParser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

@SuppressWarnings("unused")
public abstract class AbstractCanvasPanel extends JPanel implements Runnable, Serializable {
    private Thread displayThread;
    protected boolean running;
    protected double rotationAngle;
    protected Color backGround;
    protected int fps;
    protected RenderManager renderManager;
    protected RenderManager panelRenderManager;
    protected BufferedImage canvas;
    protected Graphics2D g2d;
    protected static int numberOfPanel = 0;
    protected long loopCounter;
    protected long ticksCounter;
    protected ArrayList<Runnable> firstTicks;

    private JPopupMenu popupInfo;

    public AbstractCanvasPanel() {
        running = false;
        firstTicks = new ArrayList<>();
        backGround = Color.DARK_GRAY.darker();
        rotationAngle = 0;
        fps = 60;
        loopCounter = 0;
        ticksCounter = 0;
        panelRenderManager = new RenderManager();
        renderManager = new RenderManager();
        renderManager.addRender(g2d -> g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        panelRenderManager.addRender(g2d -> g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        setPreferredSize(new Dimension(720 * 16 / 9, 720));
        setName(numberOfPanel++ + "");
    }

    public JPopupMenu getPopupInfo() {
        return popupInfo;
    }

    protected void beforePaint() {
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backGround);
        g2d.fillRect(0, 0, getWidth(), getHeight());
//        g2d.rotate(rotationAngle, getWidth() / 2.0, getHeight() / 2.0);
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        displayThread = new Thread(this, "DisplayThread");
        displayThread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            displayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public void addRender(Render... renders) {
        renderManager.addRender(renders);
        repaint();
    }

    public void addRender(int index, Render render) {
        renderManager.addRender(index, render);
    }

    public void removeRender(int... indexes) {
        renderManager.removeRender(indexes);
    }

    public void removeAllRenders() {
        renderManager.removeAllRenders();
    }

    public void addRenderToPanel(Render... renders) {
        panelRenderManager.addRender(renders);
    }

    public void removeRenderFromPanel(int... indexes) {
        panelRenderManager.removeRender(indexes);
    }

    public void removeAllRendersFromPanel() {
        panelRenderManager.removeAllRenders();
    }

    public ArrayList<Runnable> getFirstTicksList() {
        return firstTicks;
    }

    public void addTickToFirstTicksList(Runnable... ticks) {
        firstTicks.addAll(Arrays.asList(ticks));
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    @Override
    public void setBackground(Color bg) {
        backGround = bg;
        repaint();
    }

    public Color getBackGround() {
        return backGround;
    }

    public long getLoopCounter() {
        return loopCounter;
    }

    public long getTicksCounter() {
        return ticksCounter;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (g == null)
            return;
        beforePaint();
//        ((Graphics2D) g).rotate(-rotationAngle, getWidth() / 2.0, getHeight() / 2.0);
        try {
            renderManager.render(g2d);
            panelRenderManager.render((Graphics2D) g);
        } catch (ConcurrentModificationException ignored) {}
        g.drawImage(canvas, 0, 0, this);
//        ((Graphics2D) g).rotate(rotationAngle, getWidth() / 2.0, getHeight() / 2.0);
        g2d.dispose();
        g.dispose();
    }
}
