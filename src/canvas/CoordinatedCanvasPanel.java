package canvas;

import jmath.datatypes.tuples.Point2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

@SuppressWarnings("unused")
public class CoordinatedCanvasPanel extends DynamicCanvasPanel implements CoordinatedScreen {
    public static final Color DARK = Color.DARK_GRAY.darker().darker();
    public static final Color LIGHT = Color.WHITE.darker();

    private boolean showGrid;
    private boolean showAxis;
    private boolean showMousePos;
    protected boolean isDark;
    protected boolean isDynamic;

    protected double xScale;
    protected double yScale;
    protected int shiftX;
    protected int shiftY;

    private Point mousePoint;

    public CoordinatedCanvasPanel(boolean setMouseListener, boolean setKeyListener) {
        setLayout(new BorderLayout());
        xScale = 50;
        yScale = 50;
        showAxis = true;
        showGrid = false;
        setDark(true);
        isDynamic = false;
        showMousePos = true;
        if (setMouseListener)
            handleMouseListener();
        if (setKeyListener)
            handleKeyListener();
        renderManager.addRender(this::drawGrid, this::drawAxis);
    }

    public CoordinatedCanvasPanel() {
        this(true, true);
    }

    private void handleMouseListener() {
        shiftX = 0;
        shiftY = 0;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePoint = e.getPoint();
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 &&
                        !e.isAltDown() && !e.isControlDown() && !e.isShiftDown()) {
                    shiftY = 0;
                    shiftX = 0;
                    xScale = 100;
                    yScale = 100;
                    rotationAngle = 0;
                    repaint();
                }

                if (!showMousePos || e.getButton() != MouseEvent.BUTTON1 ||
                        e.isAltDown() || e.isControlDown() || e.isShiftDown())
                    return;
                var mp = getMousePosition();
                var g = (Graphics2D) getGraphics();
                g.setColor(isDark ? LIGHT.darker() : DARK.brighter());
                g.setStroke(new BasicStroke(0.8f));
                try {
                    g.drawString("x = " + coordinateX(mp.x), mp.x, mp.y + 30);
                    g.drawString("y = " + coordinateY(mp.y), mp.x, mp.y + 45);
                } catch (NullPointerException ignore) {}
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()) {
                    shiftX += mousePoint.x - e.getX();
                    shiftY += mousePoint.y - e.getY();
                    repaint();
                }
                mousePoint = e.getPoint();
            }
        });
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double changeFactor = 1 - e.getPreciseWheelRotation() * 0.1;
                var mp = new Point2D(coordinateX(e.getX()), coordinateY(e.getY()));
                xScale *= changeFactor;
                yScale *= changeFactor;
                shiftX += screenX(mp.x) - e.getX();
                shiftY += screenY(mp.y) - e.getY();
                xScale = Math.max(xScale, Double.MIN_VALUE);
                yScale = Math.max(yScale, Double.MIN_VALUE);
                repaint();
            }
        });
    }

    private void handleKeyListener() {
//        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
//            switch (((KeyEvent) event).getKeyCode()) {
//                case KeyEvent.VK_EQUALS:
//                    if (((KeyEvent) event).isControlDown()) {
//                        xScale = Math.max(xScale, Double.MIN_VALUE);
//                        yScale = Math.max(yScale, Double.MIN_VALUE);
//                        xScale *= 1.1;
//                        yScale *= 1.1;
//                    } else if (((KeyEvent) event).isShiftDown()) {
//                        rotationAngle += 0.1;
//                    }
//                    break;
//                case KeyEvent.VK_MINUS:
//                    if (((KeyEvent) event).isControlDown()) {
//                        xScale *= 0.9;
//                        yScale *= 0.9;
//                    } else if (((KeyEvent) event).isShiftDown()) {
//                        rotationAngle -= 0.1;
//                    }
//                    break;
//                case KeyEvent.VK_A: case KeyEvent.VK_LEFT: shiftX -= xScale / 5; break;
//                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: shiftX += xScale / 5; break;
//                case KeyEvent.VK_UP: shiftY -= yScale / 5; break;
//                case KeyEvent.VK_DOWN: shiftY += yScale / 5; break;
//                case KeyEvent.VK_W: xScale += xScale / 10; yScale += yScale / 10; break;
//                case KeyEvent.VK_S: xScale -= xScale / 10; yScale -= yScale / 10; break;
//            }
//            xScale = Math.max(xScale, Double.MIN_VALUE);
//            yScale = Math.max(yScale, Double.MIN_VALUE);
//            repaint();
//        }, AWTEvent.KEY_EVENT_MASK);
//
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                switch (e.getKeyCode()) {
//                case KeyEvent.VK_EQUALS:
//                    if (e.isControlDown()) {
//                        xScale = Math.max(xScale, Double.MIN_VALUE);
//                        yScale = Math.max(yScale, Double.MIN_VALUE);
//                        xScale *= 1.1;
//                        yScale *= 1.1;
//                    } else if (e.isShiftDown()) {
//                        rotationAngle += 0.1;
//                    }
//                    break;
//                case KeyEvent.VK_MINUS:
//                    if (e.isControlDown()) {
//                        xScale *= 0.9;
//                        yScale *= 0.9;
//                    } else if (e.isShiftDown()) {
//                        rotationAngle -= 0.1;
//                    }
//                    break;
//                case KeyEvent.VK_A: case KeyEvent.VK_LEFT: shiftX -= xScale / 5; break;
//                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: shiftX += xScale / 5; break;
//                case KeyEvent.VK_UP: shiftY -= yScale / 5; break;
//                case KeyEvent.VK_DOWN: shiftY += yScale / 5; break;
//                case KeyEvent.VK_W: xScale += xScale / 10; yScale += yScale / 10; break;
//                case KeyEvent.VK_S: xScale -= xScale / 10; yScale -= yScale / 10; break;
//            }
//            xScale = Math.max(xScale, Double.MIN_VALUE);
//            yScale = Math.max(yScale, Double.MIN_VALUE);
//            repaint();
//            }
//        });
    }

    protected Color getAxisColor() {
        return isDark ? LIGHT.darker() : DARK.brighter();
    }

    protected void drawAxis(Graphics2D g2d) {
        if (!showAxis)
            return;
        g2d.setColor(getAxisColor());
        g2d.setStroke(new BasicStroke(1.8f));
        g2d.drawLine(0, getHeight() / 2 - shiftY, getWidth(), getHeight() / 2 - shiftY);
        g2d.drawLine(getWidth() / 2 - shiftX, 0, getWidth() / 2 - shiftX, getHeight());
        g2d.fillPolygon(new int[]{getWidth() - 20, getWidth() - 20, getWidth()},
                new int[]{getHeight() / 2 - 5 - shiftY, getHeight() / 2 + 5 - shiftY, getHeight() / 2 - shiftY}, 3);
        g2d.fillPolygon(new int[]{getWidth() / 2 - 5 - shiftX, getWidth() / 2 + 5 - shiftX, getWidth() / 2 - shiftX},
                new int[]{20, 20, 0}, 3);
    }

    public boolean isShowMousePos() {
        return showMousePos;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public boolean isShowAxis() {
        return showAxis;
    }

    protected void drawGrid(Graphics2D g2d) {
        if (!showGrid)
            return;
        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;

        double gridXScale = Math.max(xScale / 5.0, 1);
        double gridYScale = Math.max(yScale / 5.0, 1);

        g2d.setColor(isDark ? LIGHT.darker().darker() : DARK.brighter().brighter());
        for (int i  = 0; i < getHeight() / (gridYScale * 2) + 1 + Math.abs(shiftY); i++) {
            g2d.setStroke(new BasicStroke(i % 5 == 0 ? 0.5f : 0.1f));
            var dd = (int) (i * gridYScale);
            g2d.drawLine(0, yCenter - dd - shiftY, getWidth(), yCenter - dd - shiftY);
            g2d.drawLine(0, yCenter + dd - shiftY, getWidth(), yCenter + dd - shiftY);
        }
        for (int i = 0; i < getWidth() / (gridXScale * 2) + 1 + Math.abs(shiftX); i++) {
            g2d.setStroke(new BasicStroke(i % 5 == 0 ? 0.5f : 0.1f));
            var dd = (int) (i * gridXScale);
            g2d.drawLine(xCenter - dd - shiftX, 0,  xCenter - dd - shiftX, getHeight());
            g2d.drawLine(xCenter + dd - shiftX, 0,  xCenter + dd - shiftX, getHeight());
        }
    }

    @Override
    public final double coordinateX(int screenX) {
        return (screenX + shiftX - getWidth() / 2.0) / xScale;
    }

    @Override
    public final double coordinateY(int screenY) {
        return -(screenY + shiftY - getHeight() / 2.0) / yScale;
    }

    @Override
    public final int screenX(double value) {
        return (int) (getWidth() / 2.0 + value * xScale - shiftX);
    }

    @Override
    public final int screenY(double value) {
        return (int) (getHeight() / 2.0 - value * yScale - shiftY);
    }

    public final void zoom(double factor) {
        xScale *= factor;
        yScale *= factor;
        repaint();
    }

    public final double getDelta() {
        return coordinateX(1) - coordinateX(0);
    }

    public void removeListeners() {
        for (var l : getKeyListeners())
            removeKeyListener(l);
        for (var l : getMouseWheelListeners())
            removeMouseWheelListener(l);
        for (var l : getMouseMotionListeners())
            removeMouseMotionListener(l);
        for (var l : getMouseListeners())
            removeMouseListener(l);
    }

    public double getXScale() {
        return xScale;
    }

    public void setXScale(double xScale) {
        this.xScale = Math.max(Math.abs(xScale), Double.MIN_VALUE);
        repaint();
    }

    public void setYScale(double yScale) {
        this.yScale = Math.max(Math.abs(yScale), Double.MIN_VALUE);
        repaint();
    }

    public double getYScale() {
        return yScale;
    }

    public final void setXYScale(double xScale, double yScale) {
        setXScale(xScale);
        setYScale(yScale);
    }

    public int getShiftX() {
        return shiftX;
    }

    public void setShiftX(int shiftX) {
        this.shiftX = shiftX;
        repaint();
    }

    public int getShiftY() {
        return shiftY;
    }

    public void setShiftXY(int shiftX, int shiftY) {
        setShiftX(shiftX);
        setShiftY(shiftY);
    }

    public void setShiftY(int shiftY) {
        this.shiftY = shiftY;
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    public void setShowMousePos(boolean showMousePos) {
        this.showMousePos = showMousePos;
        repaint();
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setDark(boolean isDark) {
        this.isDark = isDark;
        backGround = isDark ? DARK : LIGHT;
        repaint();
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
        if (isDynamic) {
            start();
        } else {
            stop();
        }
    }

    public void moveOnPlane(double dx, double dy) {
        shiftX += dx * xScale;
        shiftY -= dy * yScale;
        repaint();
    }

    public void setShiftX(double shiftX) {
        setShiftX(screenX(shiftX));
        repaint();
    }

    public void setShiftY(double shiftY) {
        setShiftY(screenY(shiftY));
        repaint();
    }

    public void setShiftXY(double shiftX, double shiftY) {
        setShiftXY(screenX(shiftX), screenY(shiftY));
    }

    @Override
    public void run() {
        if (isDynamic)
            super.run();
        running = false;
    }
}
