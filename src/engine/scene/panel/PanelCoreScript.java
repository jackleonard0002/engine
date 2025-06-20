package engine.scene.panel;

import engine.registry.EntityRegistry;
import engine.registry.comp.MouseInputComp;
import engine.registry.comp.script.Script;
import engine.registry.render.Renderer;
import engine.registry.render.rect.RectComp;
import engine.registry.render.rect.RectRenderConfig;

public class PanelCoreScript extends Script {

    @Override
    public void onTick(float deltaT) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        onLoad(entityRegistry);
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        RectComp rect = entityRegistry.addComponent(getEntity(), RectComp.class);
        // RectRenderer renderer = rect.getRenderer();
        RectRenderConfig renderConfig = rect.getRectRenderConfig();
        renderConfig.setFlags(Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
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

        // int closeButtonEntity = entityRegistry.createEntity();
        // int maxButtonEntity = entityRegistry.createEntity();
        // int minButtonEntity = entityRegistry.createEntity();

        // ButtonComp closeButtonComp = entityRegistry.addComponent(closeButtonEntity,
        // ButtonComp.class);
        // ImageComp bufferedRenderComp2 =
        // entityRegistry.getComponent(closeButtonEntity, ImageComp.class);
        // ImageRenderer renderer1 = bufferedRenderComp2.getRenderer();
        // ImageRendererConfig renderConfig1 = renderer1.getRenderConfig();
        // bufferedRenderComp2.getRenderer().getRenderConfig().setFlags(
        // Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING |
        // Renderer.FLAG_FOLLOW_ALL
        // | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        // // rootContainer.addChild(button);
        // renderConfig1.setLayoutColNum(6);
        // renderConfig1.setLayoutRowNum(6);
        // renderConfig1.setLayoutCol(5);
        // renderConfig1.setLayoutRow(0);
        // closeButtonComp.addButtonListener(new ButtonListener() {

        // @Override
        // public void onEnter() {
        // // TODO Auto-generated method stub
        // }

        // @Override
        // public void onExit() {
        // // TODO Auto-generated method stub
        // }

        // @Override
        // public void onHover() {
        // renderConfig1.setImageID("Big_Del_Button_G");
        // }

        // @Override
        // public void onAway() {
        // renderConfig1.setImageID("Big_Del_Button");
        // }

        // @Override
        // public void onHold() {
        // // TODO Auto-generated method stub
        // }

        // @Override
        // public void onPress() {
        // // TODO Auto-generated method stub
        // }

        // @Override
        // public void onAbort() {
        // // TODO Auto-generated method stub
        // }

        // });s

        // Container panelContainer = entityRegistry.getComponent(getEntity(),
        // Container.class);
        // panelContainer.addChild(closeButtonEntit);
        // panelContainer.addChild(maxButtonEntity);
        // panelContainer.addChild(minButtonEntity);
    }

}
