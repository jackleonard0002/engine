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

public class Registry implements Serializable {
    private final AtomicInteger entityIdGenerator = new AtomicInteger(0);
    private final HashMap<Integer, EntityMaterial> entityMap = new HashMap<>();

    public Map<Integer, EntityMaterial> getEntityMap() {
        return entityMap;
    }

    public EntityMaterial getEntityMaterial(Integer entityID) {
        return entityMap.get(entityID);
    }

    public Integer createEntity() {
        final int id = entityIdGenerator.incrementAndGet();
        entityMap.put(id, new EntityMaterial());
        return id;
    }

    /**
     * Isn't saved.
     * 
     * @return The created entity int.
     */
    public Integer createVoliteEntity() {
        final int id = entityIdGenerator.incrementAndGet();
        EntityMaterial entityM = new EntityMaterial();
        entityM.setFlags(EntityMaterial.FLAG_DONT_SAVE);
        entityMap.put(id, entityM);
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
        if (entityMap.get(entityID) != null) {
            Set<Component> components = new HashSet<>(entityMap.get(entityID).getComponents());
            for (Component component : components) {
                removeComponent(entityID, component.getClass());
            }
        }
        entityMap.remove(entityID);
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
        for (int entity : new LinkedList<>(entityMap.keySet())) {
            // Load Containers first. Allows for
            Container con = getComponent(entity, Container.class);
            if (con != null) {
                con.setEntity(entity);
                con.onLoad(this);
            }
            for (Component comp : new HashSet<>(entityMap.get(entity).getComponents())) {
                if (comp.getClass() != Container.class) {
                    comp.setEntity(entity);
                    comp.onLoad(this);
                }
            }
        }
    }

    public void unloadALLComponents() {
        for (int entity : new LinkedList<>(entityMap.keySet())) {
            if (entityMap.get(entity) != null) {
                for (Component comp : new HashSet<>(entityMap.get(entity).getComponents())) {
                    comp.setEntity(entity);
                    comp.onUnload(this);
                }
            }
        }
        // Removes all empty enities without any components.
        entityMap.entrySet().removeIf(entry -> entry.getValue().getComponents().isEmpty());
    }

    public <T extends Component> T addComponent(Integer entity, Class<T> comp) {
        T newComp = null;
        try {

            Set<Component> comps = entityMap.get(entity).getComponents();
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

            entityMap.get(entity).getComponents().add(newComp);
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
        Set<Component> componentList = entityMap.get(entity).getComponents();
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
        Set<Component> cs = entityMap.get(entity).getComponents();
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
            entityMap.get(entity).getComponents().remove(c);
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
        if (entityMap.get(entity) == null) {
            Logger.log(Logger.ERR, "Enity: '" + entity + "' is not in registry or is null");
        }
        Set<Component> components = new HashSet<>(entityMap.get(entity).getComponents());
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
        Set<Component> components = entityMap.get(entity).getComponents();
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
        Set<Component> components = entityMap.get(entity).getComponents();
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
