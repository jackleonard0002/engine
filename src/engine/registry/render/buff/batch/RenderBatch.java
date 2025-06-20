package engine.registry.render.buff.batch;

import engine.util.Vec2f;

public record RenderBatch(String imageID, Vec2f scale,
        Vec2f rotation, float opaciy) implements java.io.Serializable {

    public RenderBatch() {
        this("N/A",
                Vec2f.allValues(1), Vec2f.allValues(0), 1);
    }

    public RenderBatch(String imageID) {
        this(imageID, Vec2f.allValues(1), Vec2f.allValues(0), 1);
    }

}
