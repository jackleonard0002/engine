package engine.registry.render.buff.batch;

import java.util.HashMap;
import java.util.Map;

import engine.registry.render.buff.BufferedRenderConfig;
import engine.util.Vec2f;

public final class BatchRenderConfig extends BufferedRenderConfig {
    private HashMap<Vec2f, RenderBatch> tile_map = new HashMap<>();
    private Vec2f tileSize = new Vec2f(48, 32);

    public Map<Vec2f, RenderBatch> getTile_map() {
        return tile_map;
    }

    public void putRenderBatch(Vec2f pos, RenderBatch imageID) {
        if (!tile_map.containsKey(pos)) {
            tile_map.put(pos, imageID);
            eventListener.triggerEvent(this);
        }
    }

    public Vec2f getTileSize() {
        return tileSize;
    }

    public void setTileSize(Vec2f tileSize) {
        if (this.tileSize.equals(tileSize)) {
            this.tileSize = tileSize;
            eventListener.triggerEvent(this);
        }
    }
}
