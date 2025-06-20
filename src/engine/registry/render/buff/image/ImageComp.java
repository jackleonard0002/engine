package engine.registry.render.buff.image;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.render.HasRenderer;

public class ImageComp extends Component implements HasRenderer<ImageRenderer> {

    transient private ImageRenderer renderer;
    private ImageRendererConfig imageRenderConfig = new ImageRendererConfig();

    @Override
    public void onReset(Registry registry) {
        renderer = new ImageRenderer();
        renderer.setEntity(getEntity());
        renderer.reset(registry);
        renderer.setRenderConfig(imageRenderConfig);
        renderer.interFun();
    }

    @Override
    public void onLoad(Registry registry) {
        renderer = new ImageRenderer();
        renderer.setEntity(getEntity());
        renderer.reset(registry);
        renderer.setRenderConfig(imageRenderConfig);
    }

    @Override
    public ImageRenderer getRenderer() {
        return renderer;
    }

}
