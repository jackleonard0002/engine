package engine.registry.render.buff.batch;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import engine.registry.render.buff.BufferedRenderer;
import engine.util.Vec2f;
import engine.util.gfx.Assets;

public final class BatchRenderer extends BufferedRenderer<BatchRenderConfig> {

    public BatchRenderer() {
        super();
        pushResetCallBack((entityRegistry) -> {
            getRunnableStack().add(() -> {
                setCachedImage(Assets.getCachedImage("0"));
            });
        });
    }

    public void bake() {
        float maxX = 0;
        float maxY = 0;
        float maxwidth = 8;
        float maxHeight = 8;
        float tilesizeX = getRenderConfig().getTileSize().x;
        float tilesizeY = getRenderConfig().getTileSize().y;

        // Need to add them together

        for (Vec2f e : getRenderConfig().getTile_map().keySet()) {
            if (e.x > maxX) {
                maxX = e.x;
            }
            if (e.y > maxY) {
                maxY = e.y;
            }
        }

        maxwidth = maxX * tilesizeX;
        maxHeight = maxY * tilesizeY;

        if (maxwidth == 0 || maxHeight == 0) {
            // image = null;
            return;
        }

        Assets.putCachedImage("0", new BufferedImage((int) maxwidth, (int) maxHeight, BufferedImage.TYPE_INT_ARGB));
        Iterator<Entry<Vec2f, RenderBatch>> it = getRenderConfig().getTile_map().entrySet().iterator();
        while (it.hasNext()) {
            Entry<Vec2f, RenderBatch> item = (Entry<Vec2f, RenderBatch>) it.next();
            // it.remove() will delete the item from the map
            addImageToLayers(item.getValue(), item.getKey());
        }
    }

    private void addImageToLayers(RenderBatch bp, Vec2f position) {
        Graphics2D g2d = (Graphics2D) Assets.getCachedImage("0").createGraphics();

        // AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        AffineTransform at = AffineTransform.getTranslateInstance((position.x - 1) * getRenderConfig().getTileSize().x,
                (position.y - 1) * getRenderConfig().getTileSize().y); // 40,
        at.rotate(Math.toRadians(0));
        at.scale(bp.scale().x, bp.scale().y);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bp.opaciy()));
        g2d.drawImage(Assets.getImage(bp.imageID()), at, null);
        g2d.dispose();

    }

}
