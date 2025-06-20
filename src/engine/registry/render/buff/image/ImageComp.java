package engine.registry.render.buff.image;

import engine.registry.Component;
import engine.registry.EntityRegistry;
import engine.registry.render.HasRenderer;

public class ImageComp extends Component implements HasRenderer<ImageRenderer> {

    transient private ImageRenderer renderer;
    private ImageRendererConfig imageRenderConfig = new ImageRendererConfig();

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        renderer = new ImageRenderer();
        renderer.setEntity(getEntity());
        renderer.reset(entityRegistry);
        renderer.setRenderConfig(imageRenderConfig);
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        renderer = new ImageRenderer();
        renderer.setEntity(getEntity());
        renderer.reset(entityRegistry);
        renderer.setRenderConfig(imageRenderConfig);
        renderer.interFun();
    }

    @Override
    public void onUnload(EntityRegistry entityRegistry) {
        // TODO Auto-generated method stub
    }

    @Override
    public ImageRenderer getRenderer() {
        return renderer;
    }

}
