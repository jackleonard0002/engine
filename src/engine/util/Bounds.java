package engine.util;

import java.io.Serializable;

import engine.scene.AutoInstantiate;

public class Bounds implements Serializable {
    private Vec2f position = new Vec2f(0, 0);
    private Vec2f size = new Vec2f(1920 / 2, 1080 / 2);

    @AutoInstantiate
    transient private EventListener<Bounds> eventListener = new EventListener<>();

    public EventListener<Bounds> getEventListener() {
        return eventListener;
    }

    public Bounds(Vec2f position, Vec2f size) {
        this.position = position;
        this.size = size;
    }

    public Bounds() {
        eventListener = new EventListener<>();
    }

    public Bounds(float newX, float newY, float newWidth, float newHeight) {
        position.x = newX;
        position.y = newY;
        size.x = newWidth;
        size.y = newHeight;
    }

    public float[] toArray() {
        return new float[] { position.x, position.y, size.y, size.x };
    }

    public Vec2f getPosition() {
        return position;
    }

    public void setPosition(Vec2f position) {
        if (!Vec2f.isEqual(this.position, position)) {
            this.position = position;
            eventListener.triggerEvent(this);
        }
    }

    public Vec2f getSize() {
        return size;
    }

    public void setSize(Vec2f size) {
        if (!Vec2f.isEqual(this.size, size)) {
            this.size = size;
            eventListener.triggerEvent(this);
        }
    }
}
