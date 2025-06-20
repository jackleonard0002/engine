package engine.registry.render.buff;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import engine.registry.render.Renderer;
import engine.util.Vec2f;
import engine.util.gfx.Assets;

/**
 * Doesn't set cachedImage but supply it and also a render callback for it.
 */
public class BufferedRenderer<T extends BufferedRenderConfig> extends Renderer<T> {

    transient private BufferedImage cachedImage = Assets.NULL_IMAGE;
    transient private Image bakedImage = Assets.NULL_IMAGE;
    transient private float newX, newY, newWidth, newHeight;
    transient Consumer<Vec2f> updatConsumer;

    public BufferedRenderer() {
        super();
        pushResetCallBack((entityRegistry) -> {
            getRenderCallBacks().pushToStack((g2d) -> {
                // Logger.log("RENDEERIN IMAGE");
                if (getRenderConfig().isStretchImage()) {
                    g2d.drawImage(bakedImage, (int) (getRenderX()), (int) (getRenderY()), null);
                    g2d.drawImage(bakedImage, (int) (getRenderX()), (int) (getRenderY()), null);
                } else {
                    g2d.drawImage(bakedImage, (int) (newX), (int) (newY), null);
                }
            });
            getRunnableStack().add(() -> {
                // cachedImage = Assets.getImage(getRenderConfig().getImageID());
                if (cachedImage == null)
                    return;
                if (!getRenderConfig().isStretchImage()) {
                    // Logger.LOG("Every frame??: ");
                    int imageWidth = cachedImage.getWidth();
                    int imageHeight = cachedImage.getHeight();
                    double imageAspectRatio = (double) imageWidth / imageHeight;
                    double boundsAspectRatio = (double) getRenderWidth() / getRenderHeight();

                    float scaleFactor;

                    if (imageAspectRatio > boundsAspectRatio) {
                        scaleFactor = (float) getRenderWidth() / imageWidth;
                    } else {
                        scaleFactor = (float) getRenderHeight() / imageHeight;
                    }

                    newWidth = (float) (imageWidth * scaleFactor);
                    newHeight = (float) (imageHeight * scaleFactor);

                    bakedImage = cachedImage.getScaledInstance((int) newWidth, (int) newHeight,
                            Image.SCALE_REPLICATE);

                    newX = (float) getRenderX() + ((getRenderWidth() - bakedImage.getWidth(null)) / 2);
                    newY = (float) getRenderY() + ((getRenderHeight() - bakedImage.getHeight(null)) / 2);

                } else {
                    bakedImage = cachedImage.getScaledInstance((int) Math.abs(getRenderWidth()),
                            (int) Math.abs(getRenderHeight()),
                            Image.SCALE_REPLICATE);
                }
            });
            // interFun();
        });

    }

    public BufferedImage getCachedImage() {
        return cachedImage;
    }

    public void setCachedImage(BufferedImage cachedImage) {
        this.cachedImage = cachedImage;
    }

}
