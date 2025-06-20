package engine.registry.render.rect;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.render.HasRenderer;

public class RectComp extends Component implements HasRenderer<RectRenderer> {

    private transient RectRenderer rectRenderer;
    private RectRenderConfig rectRenderConfig = new RectRenderConfig();

    public RectRenderConfig getRectRenderConfig() {
        return rectRenderConfig;
    }

    @Override
    public void onReset(Registry registry) {
        rectRenderer = new RectRenderer();
        rectRenderer.setEntity(getEntity());
        rectRenderer.reset(registry);
        rectRenderer.setRenderConfig(rectRenderConfig);
    }

    @Override
    public void onLoad(Registry registry) {
        rectRenderer = new RectRenderer();
        rectRenderer.setEntity(getEntity());
        rectRenderer.reset(registry);
        rectRenderer.setRenderConfig(rectRenderConfig);

    }

    @Override
    public void onUnload(Registry registry) {
        // TODO Auto-generated method stub
    }

    @Override
    public RectRenderer getRenderer() {
        return rectRenderer;
    }

}
