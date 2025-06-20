package engine.registry.comp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import engine.registry.Component;
import engine.registry.Registry;
import engine.scene.AutoInstantiate;
import engine.util.Logger;

public class KeyInputComp extends Component implements KeyListener {

    @AutoInstantiate
    private transient List<KeyListener> keyListeners = new LinkedList<>();

    @AutoInstantiate
    private transient Collection<Integer> childEntities;

    @AutoInstantiate
    private transient Container container;

    @Override
    public void onReset(Registry registry) {
        container = registry.requestComponentFrom(getEntity(), Container.class, this);
        childEntities = container.getChildren();
    }

    @Override
    public void onLoad(Registry registry) {
        container = registry.requestComponentFrom(getEntity(), Container.class, this);
        childEntities = container.getChildren();
    }

    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        propagate(e, KeyInputComp::keyTyped);
        keyListeners.forEach(l -> l.keyTyped(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Logger.log(Logger.EXOT, "Press from Key input in Entity: " + getEntity());
        propagate(e, KeyInputComp::keyPressed);
        keyListeners.forEach(l -> l.keyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        propagate(e, KeyInputComp::keyReleased);
        keyListeners.forEach(l -> l.keyReleased(e));
    }

    private <E extends KeyEvent> void propagate(E event, BiConsumer<KeyInputComp, E> handler) {
        for (int childId : childEntities) {
            KeyInputComp child = container.getRegistry().getComponent(childId, KeyInputComp.class);
            if (child != null) {
                handler.accept(child, event);
            }
        }
    }
}
