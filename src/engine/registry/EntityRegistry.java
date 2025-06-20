package engine.registry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import engine.registry.comp.Container;
import engine.util.Logger;

public final class EntityRegistry implements Serializable {

    private final AtomicInteger entityIdGenerator = new AtomicInteger(0);
    private final HashMap<Integer, HashSet<Component>> registry = new HashMap<>();

    public Map<Integer, HashSet<Component>> getRegistry() {
        return registry;
    }

    public Set<Component> getEntityComponents(Integer entity) {
        return registry.get(entity);
    }

    public Integer createEntity() {
        final int id = entityIdGenerator.incrementAndGet();
        registry.put(id, new HashSet<>());
        return id;
    }

    /**
     * Isn't saved.
     * 
     * @return The created entity int.
     */
    public Integer createVoliteEntity() {
        final int id = entityIdGenerator.incrementAndGet();
        registry.put(id, new HashSet<>());
        return id;
    }

    /**
     * Removes or destroys an entity, you choose.
     * Removes all components from entity then removes it from registry.
     * Likely not very healthy.
     * EntityID is also stored in containers,
     * but I don't think anywhere else. I could limit the referance
     * to entities though containers and then get the parent through
     * the container and remove it from there.
     * If I have a int in a container that doesn't exist maybe it
     * could just be removed from the conatainer, if entityID == null:
     * remove entity, right? idk.
     * Maybe I should have a onDetach() abstract function along of a
     * onReset -maybe onDestroy?
     * 
     * @param entityID entityID to destroy.
     */
    public void destroyEntity(int entityID) {
        if (registry.get(entityID) != null) {
            Set<Component> components = new HashSet<>(registry.get(entityID));
            for (Component component : components) {
                removeComponent(entityID, component.getClass());
            }
        }
        registry.remove(entityID);
    }

    // public void resetALLComponents() {
    // for (HashSet<Component> comps : registry.values()) {
    // for (Component comp : comps) {
    // comp.onReset(this);
    // }
    // }
    // }

    /**
     * Load Containers first. Allows for
     * {@link engine.registry.render.Renderer#interFun() Renderer.interFun()}
     * at any* onload().
     */
    public void loadALLComponents() {
        for (int entity : new LinkedList<>(registry.keySet())) {
            // Load Containers first. Allows for
            Container con = getComponent(entity, Container.class);
            if (con != null) {
                con.setEntity(entity);
                con.onLoad(this);
            }
            for (Component comp : new HashSet<>(registry.get(entity))) {
                if (comp.getClass() != Container.class) {
                    comp.setEntity(entity);
                    comp.onLoad(this);
                }
            }
        }
    }

    public void unloadALLComponents() {
        for (int entity : new LinkedList<>(registry.keySet())) {
            if (registry.get(entity) != null) {
                for (Component comp : new HashSet<>(registry.get(entity))) {
                    comp.setEntity(entity);
                    comp.onUnload(this);
                }
            }
        }
        // Removes all empty enities without any components.
        registry.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public <T extends Component> T addComponent(Integer entity, Class<T> comp) {
        T newComp = null;
        try {

            Set<Component> comps = registry.get(entity);
            // if (comps == null) {
            // return null;
            // }
            for (Component component : comps) {
                if (component.getClass() == comp) {
                    Logger.log(Logger.WARM, " Attempted to add dupe component");
                    return getComponent(entity, comp);
                }
            }
            // Creates Instance
            newComp = comp.getDeclaredConstructor().newInstance();

            registry.get(entity).add(newComp);
            newComp.setEntity(entity);
            newComp.onReset(this);

            return newComp;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Component> T getComponent(Integer entity, Class<T> comp) {
        Set<Component> componentList = registry.get(entity);
        if (componentList != null) {
            for (Component component : componentList) {
                if (comp.isInstance(component)) {
                    return comp.cast(component);
                }
            }
        }
        return null;
    }

    public <T extends Component> T requiresComponent(Integer entity, Class<T> tempComponent) {
        if (!Contains(entity, tempComponent)) {
            return addComponent(entity, tempComponent);
        }
        return getComponent(entity, tempComponent);
    }

    public boolean Contains(Integer entity, Class<? extends Component> TempComponent) {
        Set<Component> cs = registry.get(entity);
        for (Component c : cs) {
            if (c.getClass() == TempComponent)
                return true;
        }
        return false;
    }

    public <T extends Component> boolean removeComponent(int entity, Class<? extends T> newComponent) {
        if (getComponent(entity, newComponent) != null) {
            Component c = getComponent(entity, newComponent);
            // vsComponentRemoved(c);
            registry.get(entity).remove(c);
            return true;
        }
        return false;
    }

    public <T extends Component> T requestComponentFrom(int entity, Class<T> tempComponent, Component comp) {
        if (!Contains(entity, tempComponent)) {
            T compsInstanace = addComponent(entity, tempComponent);
            compsInstanace.requestComponent = comp;
            return compsInstanace;
        }
        return getComponent(entity, tempComponent);
    }

    public boolean Contains(int entity, Class<? extends Component> TempComponent) {
        if (registry.get(entity) == null) {
            Logger.log(Logger.ERR, "Enity: '" + entity + "' is not in registry or is null");
        }
        Set<Component> components = new HashSet<>(registry.get(entity));
        for (Component c : components) {
            if (c == null) {
                continue;
            }
            if (c.getClass() == TempComponent)
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponentInParent(int entity, Class<T> componentclass) {
        Set<Component> components = registry.get(entity);
        if (components != null) {
            for (Component c : components) {
                if (componentclass.isAssignableFrom(c.getClass()))
                    return (T) c;

                if (c.getClass().getSuperclass() == componentclass) {
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponentWithInterface(int entity, Class<T> interfaceclass) {
        Set<Component> components = registry.get(entity);
        if (components != null) {
            for (Component c : components) {
                if (interfaceclass.isAssignableFrom(c.getClass())) {
                    return (T) c;
                }
                // if (interfaceclass.isNestmateOf(c.getClass())) {
                // return (T) c;
                // }

            }
        }
        return null;
    }
}
