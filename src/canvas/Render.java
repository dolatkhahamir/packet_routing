package canvas;

import java.awt.*;
import java.io.Serializable;

@FunctionalInterface
@SuppressWarnings("unused")
public interface Render extends Serializable {
    void render(Graphics2D g2d);
    default void tick() {}
    default void renderWithParams(Graphics2D g2d, Object... params) {}
    default void tickWithParams(Object... params) {}
}
