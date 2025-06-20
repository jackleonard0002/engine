package engine.registry.comp.script;

import engine.registry.EntityRegistry;

public abstract class Script implements java.io.Serializable {

    private int entity = 1;

    abstract public void onTick(float deltaT);

    abstract public void onReset(EntityRegistry entityRegistry);

    abstract public void onLoad(EntityRegistry entityRegistry);

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

}
