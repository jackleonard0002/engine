package engine.registry.render;

import java.util.function.Consumer;

import engine.util.EventListener;

public abstract class RenderConfig implements java.io.Serializable {

    protected int flags = Renderer.FLAG_DEFAULT;
    protected int layoutColNum = 2;
    protected int layoutRowNum = 2;
    protected int layoutCol = 1;
    protected int layoutRow = 1;

    transient protected EventListener<RenderConfig> eventListener = new EventListener<>();

    public EventListener<RenderConfig> createEventListener() {
        return eventListener = new EventListener<>();
    }

    protected RenderConfig() {

    }

    public void addListener(Consumer<RenderConfig> listener) {
        eventListener.addListener(listener);
    }

    public void removeListener(Consumer<RenderConfig> listener) {
        eventListener.removeListener(listener);
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        if (this.flags != flags) {
            this.flags = flags;
            eventListener.triggerEvent(this);
        }
    }

    public int getLayoutColNum() {
        return layoutColNum;
    }

    public void setLayoutColNum(int layoutColNum) {
        if (this.layoutColNum != layoutColNum) {
            this.layoutColNum = layoutColNum;
            eventListener.triggerEvent(this);
        }
    }

    public int getLayoutRowNum() {
        return layoutRowNum;
    }

    public void setLayoutRowNum(int layoutRowNum) {
        if (this.layoutRowNum != layoutRowNum) {
            this.layoutRowNum = layoutRowNum;
            eventListener.triggerEvent(this);
        }
    }

    public int getLayoutCol() {
        return layoutCol;
    }

    public void setLayoutCol(int layoutCol) {
        if (this.layoutCol != layoutCol) {
            this.layoutCol = layoutCol;
            eventListener.triggerEvent(this);
        }
    }

    public int getLayoutRow() {
        return layoutRow;
    }

    public void setLayoutRow(int layoutRow) {
        if (this.layoutRow != layoutRow) {
            this.layoutRow = layoutRow;
            eventListener.triggerEvent(this);
        }
    }
}
