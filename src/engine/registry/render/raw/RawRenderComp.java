package engine.registry.render.raw;

import engine.registry.Component;
import engine.registry.Registry;
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
    public void onReset(Registry registry) {
        rawRenderer = new RawRenderer();
        rawRenderer.setEntity(getEntity());
        rawRenderer.reset(registry);
        rawRenderer.setRenderConfig(rawRenderConfig);
    }

    @Override
    public void onLoad(Registry registry) {
        rawRenderer = new RawRenderer();
        rawRenderer.setEntity(getEntity());
        rawRenderer.reset(registry);
        rawRenderer.setRenderConfig(rawRenderConfig);
    }

}
