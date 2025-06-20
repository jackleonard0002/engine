package engine.registry.render.rect;

import engine.registry.Component;
import engine.registry.EntityRegistry;
import engine.registry.render.HasRenderer;

public class RectComp extends Component implements HasRenderer<RectRenderer> {

    private transient RectRenderer rectRenderer;
    private RectRenderConfig rectRenderConfig = new RectRenderConfig();

    public RectRenderConfig getRectRenderConfig() {
        return rectRenderConfig;
    }

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        rectRenderer = new RectRenderer();
        rectRenderer.setEntity(getEntity());
        rectRenderer.reset(entityRegistry);
        rectRenderer.setRenderConfig(rectRenderConfig);
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        rectRenderer = new RectRenderer();
        rectRenderer.setEntity(getEntity());
        rectRenderer.reset(entityRegistry);
        rectRenderer.setRenderConfig(rectRenderConfig);

    }

    @Override
    public void onUnload(EntityRegistry entityRegistry) {
        // TODO Auto-generated method stub
    }

    @Override
    public RectRenderer getRenderer() {
        return rectRenderer;
    }

}
