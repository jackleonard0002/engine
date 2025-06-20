package engine.registry.render.raw;

import engine.registry.Component;
import engine.registry.EntityRegistry;
import engine.registry.render.HasRenderer;

/**
 * An invislbe renderer, with all the work but no result.
 * Used for Cacluations, can enable outline.
 */
public class RawRenderComp extends Component implements HasRenderer<RawRenderer> {

    private RawRenderer rawRenderer;
    private RawRenderConfig rawRenderConfig;

    public RawRenderConfig getRawRenderConfig() {
        return rawRenderConfig;
    }

    @Override
    public RawRenderer getRenderer() {
        return rawRenderer;
    }

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        rawRenderer = new RawRenderer();
        rawRenderer.setEntity(getEntity());
        rawRenderer.reset(entityRegistry);
        rawRenderer.setRenderConfig(rawRenderConfig);
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        rawRenderer = new RawRenderer();
        rawRenderer.setEntity(getEntity());
        rawRenderer.reset(entityRegistry);
        rawRenderer.setRenderConfig(rawRenderConfig);
    }

    @Override
    public void onUnload(EntityRegistry entityRegistry) {
        // TODO Auto-generated method stub
    }

}
