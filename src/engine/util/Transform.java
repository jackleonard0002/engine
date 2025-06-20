package engine.util;

import java.io.Serializable;

import engine.scene.AutoInstantiate;

public class Transform implements Serializable {
    private Vec2f range = new Vec2f(0, 0);
    private Vec2f scale = new Vec2f(1, 1);
    private Vec2f rotation = new Vec2f(0, 0);

    @AutoInstantiate
    transient private final EventListener<Transform> eventListener = new EventListener<>();

    public EventListener<Transform> getEventListener() {
        return eventListener;
    }

    public boolean equals(Transform transform) {
        return (range.x == transform.getRange().x) &&
                (range.y == transform.getRange().y) &&
                (scale.x == transform.getScale().x) &&
                (scale.y == transform.getScale().y) &&
                (rotation.x == transform.getRotation().x) &&
                (rotation.y == transform.getRotation().y);
    }

    public void copy(Transform transform) {
        setRange(new Vec2f(transform.getRange().x, transform.getRange().y));
        setScale(new Vec2f(transform.getScale().x, transform.getScale().y));
        setRotation(new Vec2f(transform.getRotation().x, transform.getRotation().y));
    }

    public float[] toArray() {
        return new float[] { range.x, range.y, scale.y, scale.x };
    }

    public Vec2f getRange() {
        return range;
    }

    public void setRange(Vec2f range) {
        if (!Vec2f.isEqual(this.range, range)) {
            this.range = range;
            eventListener.triggerEvent(this);
        }
    }

    public Vec2f getScale() {
        return scale;
    }

    public void setScale(Vec2f scale) {
        if (!Vec2f.isEqual(this.scale, scale)) {
            this.scale = scale;
            eventListener.triggerEvent(this);
        }
    }

    public Vec2f getRotation() {
        return rotation;
    }

    public void setRotation(Vec2f rotation) {
        if (!Vec2f.isEqual(this.scale, rotation)) {
            this.rotation = rotation;
            eventListener.triggerEvent(this);
        }
    }

}
