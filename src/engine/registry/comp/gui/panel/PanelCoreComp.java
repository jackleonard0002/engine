package engine.registry.comp.gui.panel;

import java.awt.Color;

import org.w3c.dom.css.Rect;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.comp.Container;
import engine.registry.comp.KeyInputComp;
import engine.registry.comp.MouseInputComp;
import engine.registry.comp.gui.ButtonComp;
import engine.registry.comp.gui.ButtonListener;
import engine.registry.render.Renderer;
import engine.registry.render.buff.image.ImageComp;
import engine.registry.render.buff.image.ImageRendererConfig;
import engine.registry.render.rect.RectComp;
import engine.registry.render.rect.RectRenderConfig;
import engine.util.Logger;

public class PanelCoreComp extends Component {

    @Override
    public void onReset(Registry entityRegistry) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLoad(Registry registry) {
        RectComp rect = registry.addComponent(getEntity(), RectComp.class);
        rect.getRectRenderConfig().setRectColor(new Color(20, 20, 20, 210).getRGB());
        // RectRenderer renderer = rect.getRenderer();
        RectRenderConfig renderConfig = rect.getRectRenderConfig();
        renderConfig.setFlags(Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                | Renderer.FLAG_LAYOUT_GRID);
        registry.addComponent(getEntity(), MouseInputComp.class);
        KeyInputComp keyInputComp = registry.requestComponentFrom(getEntity(), KeyInputComp.class, this);
        ButtonComp button = registry.addComponent(getEntity(), ButtonComp.class);
        RectRenderConfig rectRenderConfig = rect.getRectRenderConfig();
        renderConfig.setLayoutColNum(3);
        renderConfig.setLayoutRowNum(3);
        renderConfig.setLayoutCol(1);
        renderConfig.setLayoutRow(1);
        button.addButtonListener(new ButtonListener() {

            @Override
            public void onEnter() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onExit() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onHover() {
                Logger.log(Logger.EXOT, "Hovering over the panel core button");
                rectRenderConfig.setRectColor(new Color(70, 200, 70, 250).getRGB());
            }

            @Override
            public void onAway() {
                rectRenderConfig.setRectColor(new Color(20, 20, 20, 210).getRGB());
            }

            @Override
            public void onHold() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPress() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAbort() {
                // TODO Auto-generated method stub
            }
        });

        int i = registry.createVoliteEntity();
        rect = registry.addComponent(i, RectComp.class);
        rect.getRectRenderConfig().setRectColor(new Color(50, 50, 50,
                210).getRGB());
        rect.getRectRenderConfig().setFlags(Renderer.FLAG_FOLLOWING
                | Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_LAYOUT_GRID);
        rect.getRectRenderConfig().setLayoutColNum(1);
        rect.getRectRenderConfig().setLayoutRowNum(8);
        rect.getRectRenderConfig().setLayoutCol(0);
        rect.getRectRenderConfig().setLayoutRow(0);

        int closebuttonEntity = registry.createVoliteEntity();
        ButtonComp button5 = registry.addComponent(closebuttonEntity, ButtonComp.class);
        ImageComp ren = registry.addComponent(closebuttonEntity, ImageComp.class);
        ImageRendererConfig renderConfig2 = ren.getRenderer().getRenderConfig();
        button5.addButtonListener(new ButtonListener() {

            @Override
            public void onEnter() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onExit() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onHover() {
                renderConfig2.setImageID("Big_Del_Button_G");
            }

            @Override
            public void onAway() {
                renderConfig2.setImageID("Big_Del_Button");
            }

            @Override
            public void onHold() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPress() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAbort() {
                // TODO Auto-generated method stub
            }

        });
        renderConfig2.setFlags(Renderer.FLAG_FOLLOWING
                | Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_LAYOUT_GRID);
        renderConfig2.setLayoutColNum(8);
        renderConfig2.setLayoutRowNum(8);
        renderConfig2.setLayoutCol(7);
        renderConfig2.setLayoutRow(0);

        int maxbuttonEntity = registry.createVoliteEntity();
        ButtonComp button2 = registry.addComponent(maxbuttonEntity, ButtonComp.class);
        ImageComp ren2 = registry.addComponent(maxbuttonEntity, ImageComp.class);
        ImageRendererConfig renderConfig3 = ren2.getRenderer().getRenderConfig();
        button2.addButtonListener(new ButtonListener() {

            @Override
            public void onEnter() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onExit() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onHover() {
                renderConfig3.setImageID("Big_Max_Button_G");
            }

            @Override
            public void onAway() {
                renderConfig3.setImageID("Big_Max_Button");
            }

            @Override
            public void onHold() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPress() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAbort() {
                // TODO Auto-generated method stub
            }

        });
        renderConfig3.setFlags(Renderer.FLAG_FOLLOWING
                | Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_LAYOUT_GRID);
        renderConfig3.setLayoutColNum(8);
        renderConfig3.setLayoutRowNum(8);
        renderConfig3.setLayoutCol(6);
        renderConfig3.setLayoutRow(0);

        int minbutton = registry.createVoliteEntity();
        ButtonComp button3 = registry.addComponent(minbutton, ButtonComp.class);
        ImageComp ren3 = registry.addComponent(minbutton, ImageComp.class);
        ImageRendererConfig renderConfig4 = ren3.getRenderer().getRenderConfig();
        button3.addButtonListener(new ButtonListener() {

            @Override
            public void onEnter() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onExit() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onHover() {
                renderConfig4.setImageID("Big_Min_Button_G");
            }

            @Override
            public void onAway() {
                renderConfig4.setImageID("Big_Min_Button");
            }

            @Override
            public void onHold() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPress() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAbort() {
                // TODO Auto-generated method stub
            }

        });
        renderConfig4.setFlags(Renderer.FLAG_FOLLOWING
                | Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_LAYOUT_GRID);
        renderConfig4.setLayoutColNum(8);
        renderConfig4.setLayoutRowNum(8);
        renderConfig4.setLayoutCol(5);
        renderConfig4.setLayoutRow(0);

        Container container = registry.getComponent(getEntity(), Container.class);
        Logger.log(Logger.EXOT, "############## i: " + i);
        container.putChild("topbar", i);
        container.putChild("closebutton", closebuttonEntity);
        container.putChild("maxbutton", maxbuttonEntity);
        container.putChild("minbuton", minbutton);

    }

}
