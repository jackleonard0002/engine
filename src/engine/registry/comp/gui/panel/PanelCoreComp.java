package engine.registry.comp.gui.panel;

import java.awt.Color;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.comp.MouseInputComp;
import engine.registry.render.Renderer;
import engine.registry.render.rect.RectComp;
import engine.registry.render.rect.RectRenderConfig;

public class PanelCoreComp extends Component {

    private int i;

    @Override
    public void onReset(Registry entityRegistry) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLoad(Registry entityRegistry) {
        RectComp rect = entityRegistry.addComponent(getEntity(), RectComp.class);
        rect.getRectRenderConfig().setRectColor(Color.MAGENTA.getRGB());
        // RectRenderer renderer = rect.getRenderer();
        RectRenderConfig renderConfig = rect.getRectRenderConfig();
        renderConfig.setFlags(Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_DRAW_OUTLINE
                | Renderer.FLAG_LAYOUT_GRID);
        entityRegistry.addComponent(getEntity(), MouseInputComp.class);
        // entityRegistry.addComponent(panel, PanelKeyInputComp.class);
        // ScriptableComp scriptableComp = entityRegistry.addComponent(getEntity(),
        // ScriptableComp.class);
        // scriptableComp.addScript(new PanelCoreScript());
        renderConfig.setLayoutColNum(3);
        renderConfig.setLayoutRowNum(3);
        renderConfig.setLayoutCol(1);
        renderConfig.setLayoutRow(1);

        i = entityRegistry.createVoliteEntity();
        // BoundsComp boubds = entityRegistry.addComponent(i, BoundsComp.class);
    }

    @Override
    public void onUnload(Registry entityRegistry) {
        entityRegistry.destroyEntity(i);
    }

}
