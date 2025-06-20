package engine.registry.render.buff.image;

import engine.registry.render.buff.BufferedRenderer;
import engine.util.gfx.Assets;

public final class ImageRenderer extends BufferedRenderer<ImageRendererConfig> {
    public ImageRenderer() {
        super();
        pushResetCallBack((entityRegistry) -> {
            getRunnableStack().add(() -> {
                setCachedImage(Assets.getImage(getRenderConfig().getImageID()));
            });
        });
        interFun();

    }
}
