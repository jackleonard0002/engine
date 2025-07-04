package engine.registry.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import engine.registry.Component;
import engine.registry.Registry;
import engine.scene.AutoInstantiate;
import engine.util.EventListener;
import engine.util.Logger;

public class Container extends Component {

    private LinkedHashMap<String, Integer> childMap = new LinkedHashMap<>(16, 0.75f, true /* accessOrder */);
    private int parent = 0;

    // private Set<Integer> children = new HashSet<>();
    @AutoInstantiate
    transient private final EventListener<Container> eventListener = new EventListener<>();
    @AutoInstantiate
    transient private Registry registry;

    @Override
    public void onReset(Registry registry) {
        this.registry = registry;
        for (String child : new ArrayList<>(childMap.keySet())) {
            if (registry.getEntityMaterial(childMap.get(child)) == null) {
                Logger.log(Logger.ERR, "Entity " + child + " in Container " + getEntity() + " has no material");
                Logger.log(Logger.ERR, "Removing child " + child + " from Container " + getEntity());
                childMap.remove(child);
            }
        }
    }

    @Override
    public void onLoad(Registry registry) {
        this.registry = registry;
        removeNulls();
    }

    public void removeNulls() {
        for (String child : new ArrayList<>(childMap.keySet())) {
            if (registry.getEntityMaterial(childMap.get(child)) == null) {
                Logger.log(Logger.ERR,
                        "Entity " + childMap.get(child) + " in Container " + getEntity() + " has no material");
                Logger.log(Logger.ERR, "Removing child " + child + " from Container " + getEntity());
                childMap.remove(child);
            }
        }
    }

    public boolean putChild(String key, Integer entity) {
        if (getEntity() == entity) {
            Logger.log(Logger.ERR, "Cannot Entity to it's won container");
            return false;
        }
        Container childHasContainer = registry.getComponentInParent(entity, Container.class);
        if (childHasContainer != null) {
            childHasContainer.parent = getEntity();
        }
        if (!childMap.containsKey(key)) {
            childMap.put(key, entity);
            eventListener.triggerEvent(this);
            return true;
        }
        return false;
    }

    public boolean removeChild(String key) {
        if (childMap.containsKey(key)) {
            Container childHasContainer = registry.getComponentInParent(childMap.get(key), Container.class);
            if (childHasContainer != null) {
                childHasContainer.parent = 0;
            }
            childMap.remove(key);
            eventListener.triggerEvent(this);
            return true;
        }
        return false;
    }

    public Collection<Integer> getChildren() {
        removeNulls();
        return childMap.values();
    }

    public Integer getChildrenFromTag(String tag) {
        return childMap.get(tag);
    }

    public Registry getRegistry() {
        return registry;
    }

    public EventListener<Container> getEventListener() {
        return eventListener;
    }

    public int getParent() {
        return parent;
    }

}
