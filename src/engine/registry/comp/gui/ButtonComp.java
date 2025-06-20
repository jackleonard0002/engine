package engine.registry.comp.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.comp.MouseInputComp;
import engine.registry.render.buff.image.ImageComp;
import engine.registry.render.buff.image.ImageRenderer;
import engine.registry.render.buff.image.ImageRendererConfig;
import engine.scene.AutoInstantiate;

public class ButtonComp extends Component {

    transient private MouseInputComp mouseInputComp;
    // transient private ImageRendererConfig imageRendererConfig;
    transient private ImageRenderer imageRenderer;
    transient private ImageComp imageComp;

    @AutoInstantiate
    transient private final List<ButtonListener> buttonListeners = new LinkedList<>();
    transient private boolean deactive = false;
    transient private boolean prePressed = false;

    // private String hoverImageID = "Big_Del_Button_G";
    // private String hoverImageID = "Big_Del_Button_G";

    public boolean Over(float x, float y) {
        if (imageRenderer == null)
            return false;

        float renderposx = imageRenderer.getRenderX();
        float renderposy = imageRenderer.getRenderY();
        float width = imageRenderer.getRenderWidth();
        float height = imageRenderer.getRenderHeight();

        Rectangle2D box1 = new Rectangle2D.Float(renderposx, renderposy, width, height);
        Rectangle2D box2 = new Rectangle2D.Float(x, y, 2, 2);

        if (box1.intersects(box2)) {
            return true;
        }

        return false;
    }

    @Override
    public void onLoad(Registry registry) {
        onReset(registry);
    }

    @Override
    public void onUnload(Registry registry) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onReset(Registry registry) {
        imageComp = registry.requestComponentFrom(getEntity(), ImageComp.class, this);
        imageRenderer = imageComp.getRenderer();
        if (imageRenderer != null) {
            // Logger.log("First Class: " +
            // imageRenderer.getRenderConfig().getClass().getName());
            if (imageRenderer != null && imageRenderer.getRenderConfig() instanceof ImageRendererConfig) {
                imageRenderer.getRenderConfig();
                // Logger.log("Second Class: " + imageRendererConfig.getClass().getName());
            }
        }
        mouseInputComp = registry.requestComponentFrom(getEntity(), MouseInputComp.class, this);
        mouseInputComp.activation = active -> {
            if (!active) {
                // Logger.LOG("deactivivation????");
                onExit();
                onAway();
                deactive = true;
            } else {
                onEnter();
                // Logger.LOG("activivation????");
                deactive = false;
            }
        };
        // mouseInputComp.addMouseMotionListener(new MouseMotionListener() {

        // @Override
        // public void mouseDragged(MouseEvent e) {
        // // TODO Auto-generated method stub
        // }

        // @Override
        // public void mouseMoved(MouseEvent e) {
        // imageComp = entityRegistry.requestComponentFrom(getEntity(), ImageComp.class,
        // null);
        // imageRenderer = imageComp.getRenderer();
        // imageRendererConfig = imageRenderer.getRenderConfig();
        // if (imageRendererConfig == null) {
        // return;
        // }
        // if (Over(e.getX(), e.getY())) {
        // imageRendererConfig.setImageID("Big_Del_Button_G");
        // } else {
        // imageRendererConfig.setImageID("Big_Del_Button");
        // }
        // }

        // });
        mouseInputComp.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (Over(e.getX(), e.getY())) {
                    onHold();
                    // imageRenderComp.setImage(holdImage);
                    prePressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (deactive)
                    return;
                if (prePressed) {
                    if (Over(e.getX(), e.getY())) {
                        // Logger.LOG("Full Pressed");
                        onPress();
                    } else {
                        onAbort();
                    }
                    prePressed = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                onExit();
                onAway();
            }

        });
        mouseInputComp.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!Over(e.getX(), e.getY())) {
                    // uiRenderComp.setImage(selectedImage);
                    onAbort();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (deactive)
                    return;
                if (Over(e.getX(), e.getY())) {
                    // Logger.LOG("Over");
                    onHover();
                } else {
                    onAway();
                }
            }

        });
    }

    public void addButtonListener(ButtonListener buttonListener) {
        buttonListeners.add(buttonListener);
    }

    public void removeButtonListener(ButtonListener buttonListener) {
        buttonListeners.remove(buttonListener);
    }

    private void onEnter() {
        buttonListeners.forEach(l -> l.onEnter());
    }

    private void onExit() {
        buttonListeners.forEach(l -> l.onExit());
    }

    private void onHover() {
        for (ButtonListener buttonListener : buttonListeners) {
            buttonListener.onHover();
        }
    }

    private void onAway() {
        for (ButtonListener buttonListener : buttonListeners) {
            buttonListener.onAway();
        }
    }

    private void onHold() {
        for (ButtonListener buttonListener : buttonListeners) {
            buttonListener.onHold();
        }
    }

    private void onPress() {
        for (ButtonListener buttonListener : buttonListeners) {
            buttonListener.onPress();
        }
    }

    private void onAbort() {
        for (ButtonListener buttonListener : buttonListeners) {
            buttonListener.onAbort();
        }
    }

}
