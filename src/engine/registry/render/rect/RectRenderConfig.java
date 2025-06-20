package engine.registry.render.rect;

import java.awt.Color;

import engine.registry.render.RenderConfig;

public class RectRenderConfig extends RenderConfig {

    public static final int RECT_FILL = 1 << 1;

    private int rectColor = new Color(20, 20, 20, 230).getRGB();
    private int strokeThinkness = 16;
    private int rectFlags = RECT_FILL;

    public int getRectFlags() {
        return rectFlags;
    }

    public void setRectFlags(int rectFlags) {
        this.rectFlags = rectFlags;
    }

    public RectRenderConfig() {
        super();
    }

    public int getRectColor() {
        return rectColor;
    }

    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
    }

    public int getStrokeThinkness() {
        return strokeThinkness;
    }

    public void setStrokeThinkness(int strokeThinkness) {
        if (this.strokeThinkness != strokeThinkness) {
            this.strokeThinkness = strokeThinkness;
            eventListener.triggerEvent(this);
        }
    }
}
