package engine.registry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import engine.registry.comp.Container;
import engine.scene.AutoInstantiate;
import engine.util.Logger;

public class Registry implements Serializable {

    @AutoInstantiate
    transient private SecureRandom random = new SecureRandom();
    @AutoInstantiate
    transient private Set<Integer> seen = new HashSet<>();
    private final AtomicInteger entityIdGenerator = new AtomicInteger(0);
    private final HashMap<Integer, EntityMaterial> entityMap = new HashMap<>();

    public int nextId() {
        if (random == null) {
            random = new SecureRandom(); // Ensure random is initialized
        }
        if (seen == null) {
            seen = new HashSet<>(); // Ensure seen is initialized
        }
        int id;
        do {
            id = random.nextInt(Integer.MAX_VALUE); // or a smaller range if needed
        } while (!seen.add(id)); // retries until a new unique one
        return id;
    }

    public Map<Integer, EntityMaterial> getEntityMap() {
        return entityMap;
    }

    public EntityMaterial getEntityMaterial(Integer entityID) {
        return entityMap.get(entityID);
    }

    public Integer createEntity() {
        final int id = nextId();
        entityMap.put(id, new EntityMaterial());
        return id;
    }

    /**
     * Isn't saved.
     * 
     * @return The created entity int.
     */
    public Integer createVoliteEntity() {
        final int id = nextId();
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

    /**
     * Prepares the registry for unloading.
     * Removes all empty entities without any components.
     * Also Removes all entities with the
     * {@link EntityMaterial#FLAG_DONT_SAVE} flag set.
     */
    public void prepareforUnload() {
        for (int entity : new LinkedList<>(entityMap.keySet())) {
            // Remove Nulls from Container
            // This is important for the Container to not have nulls
            // in it when saving.
            Container con = getComponent(entity, Container.class);
            if (con != null) {
                con.removeNulls();
            }
        }
        entityMap.entrySet().removeIf((entry) -> {
            return (entry.getValue().getFlags() & EntityMaterial.FLAG_DONT_SAVE) != 0;
        });
        entityMap.entrySet().removeIf(entry -> entry.getValue().getComponents().isEmpty());
    }

    public <T extends Component> T addComponent(Integer entity, Class<T> comp) {
        T newComp = null;
        try {

            if (entityMap.get(entity) == null || !entityMap.containsKey(entity)) {
                return null;
            }
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
        if (entityMap.get(entity) == null || !entityMap.containsKey(entity)) {
            // Logger.log(Logger.ERR, "Entity: '" + entity + "' is not in registry or is
            // null");
            return null;
        }
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
            if (compsInstanace == null) {
                Logger.log(Logger.ERR, "Failed to add component: " + tempComponent.getName() + " to entity: " + entity);
                return null;
            }
            compsInstanace.requestComponent = comp;
            return compsInstanace;
        }
        return getComponent(entity, tempComponent);
    }

    public boolean Contains(int entity, Class<? extends Component> TempComponent) {
        if (entityMap.get(entity) == null || !entityMap.containsKey(entity)) {
            // Logger.log(Logger.ERR, "Enity: '" + entity + "' is not in registry or is
            // null");
            return false;
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
        if (entityMap.get(entity) == null) {
            // This gets triggered all the time, so I commented it out.
            // It is not a problem, just a warning.
            // Logger.log(Logger.ERR, "Entity: '" + entity + "' is not in registry or is
            // null");
            return null;
        }
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
