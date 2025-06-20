package engine.registry.comp;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import engine.registry.Component;
import engine.registry.Registry;
import engine.scene.AutoInstantiate;

public class MouseInputComp extends Component implements MouseMotionListener,
        MouseListener,
        MouseWheelListener {

    @AutoInstantiate
    transient private LinkedList<MouseListener> miListeners = new LinkedList<>();
    @AutoInstantiate
    transient private LinkedList<MouseMotionListener> mouseMouseListeners = new LinkedList<>();
    @AutoInstantiate
    transient private LinkedList<MouseWheelListener> mouseWheelListeners = new LinkedList<>();

    @AutoInstantiate
    transient private Collection<Integer> containerX;

    @AutoInstantiate
    transient private Container container;

    transient public Consumer<Boolean> activation = (input) -> {
    };

    public void active() {
        activation.accept(true);
    }

    public void deactive() {
        activation.accept(false);
    }

    @Override
    public void onReset(Registry registry) {
        container = registry.requestComponentFrom(getEntity(), Container.class, this);
        containerX = container.getChildren();
    }

    @Override
    public void onLoad(Registry registry) {
        container = registry.requestComponentFrom(getEntity(), Container.class, this);
        containerX = container.getChildren();
    }

    @Override
    public void onUnload(Registry registry) {
        // TODO Auto-generated method stub
    }

    public void addMouseListener(MouseListener ml) {
        miListeners.add(ml);
    }

    public void removeMouseListener(MouseListener ml) {
        miListeners.remove(ml);
    }

    public void addMouseMotionListener(MouseMotionListener ml) {
        mouseMouseListeners.add(ml);
    }

    public void removeMouseMotionListener(MouseMotionListener ml) {
        mouseMouseListeners.remove(ml);
    }

    public void addMouseWheelListener(MouseWheelListener ml) {
        mouseWheelListeners.add(ml);
    }

    public void removeMouseWheelListener(MouseWheelListener ml) {
        mouseWheelListeners.remove(ml);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        propagateToComponents(e, MouseInputComp::mouseWheelMoved);
        mouseWheelListeners.forEach(l -> l.mouseWheelMoved(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseClicked);
        miListeners.forEach(l -> l.mouseClicked(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mousePressed);
        miListeners.forEach(l -> l.mousePressed(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseReleased);
        miListeners.forEach(l -> l.mouseReleased(e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseEntered);
        miListeners.forEach(l -> l.mouseEntered(e));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseExited);
        miListeners.forEach(l -> l.mouseExited(e));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseDragged);
        mouseMouseListeners.forEach(l -> l.mouseDragged(e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        propagateToComponents(e, MouseInputComp::mouseMoved);
        mouseMouseListeners.forEach(l -> l.mouseMoved(e));
    }

    // Helper method
    private <E extends InputEvent> void propagateToComponents(E event, BiConsumer<MouseInputComp, E> handler) {
        for (int entity : containerX) {
            MouseInputComp comp = container.getRegistry().getComponent(entity, MouseInputComp.class);
            if (comp != null) {
                handler.accept(comp, event);
            }
        }
    }

}
