package engine.registry.render.rect;

import java.awt.BasicStroke;
import java.awt.Color;

import engine.registry.render.Renderer;

public final class RectRenderer extends Renderer<RectRenderConfig> {

    transient private Color color;
    transient private BasicStroke stroke = new BasicStroke(16);

    public RectRenderer() {
        super();
        pushResetCallBack((entistyRegistry) -> {
            stroke = new BasicStroke(16);
            getRenderCallBacks().pushToStack((g2d) -> {
                g2d.setColor(color);
                g2d.setStroke(stroke);
                if ((getRenderConfig().getRectFlags() & RectRenderConfig.RECT_FILL) != 0) {
                    g2d.fillRect((int) getRenderX(), (int) getRenderY(),
                            (int) getRenderWidth(), (int) getRenderHeight());
                } else {
                    g2d.drawRect((int) getRenderX(), (int) getRenderY(),
                            (int) getRenderWidth(), (int) getRenderHeight());
                }
            });
            getRunnableStack().push(() -> {
                color = new Color(getRenderConfig().getRectColor(), true);
            });
        });
    }

}
