package engine.registry.comp.script;

import engine.registry.Registry;

public abstract class Script implements java.io.Serializable {

    private int entity = 1;

    abstract public void onTick(float deltaT);

    abstract public void onReset(Registry entityRegistry);

    abstract public void onLoad(Registry entityRegistry);

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

}
