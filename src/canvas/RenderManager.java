package canvas;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class RenderManager implements Render {
    private final List<Render> renderList;

    public RenderManager() {
        renderList = new LinkedList<>();
    }

    public List<Render> getRenderList() {
        return renderList;
    }

    public void addRender(Render... renders) {
        for (var render : renders)
            if (render != null && !renderList.contains(render))
                renderList.add(render);
    }

    public void addRender(int index, Render render) {
        renderList.add(index, render);
    }

    public void removeRender(int... indexes) {
        for (var i : indexes)
            renderList.remove(i);
    }

    public void removeAllRenders() {
        renderList.clear();
    }

    @Override
    public void render(Graphics2D g2d) {
        try {
            renderList.forEach(e -> e.render(g2d));
        } catch (ConcurrentModificationException | NullPointerException | IndexOutOfBoundsException ignored) {}
    }

    @Override
    public void tick() {
        try {
            renderList.forEach(Render::tick);
        } catch (ConcurrentModificationException | NullPointerException | IndexOutOfBoundsException ignored) {}
    }
}
