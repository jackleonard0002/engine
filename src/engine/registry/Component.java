package engine.registry;

import engine.scene.AutoInstantiate;

/**
 * Only practical to have one single inherited layer.
 * Leave Constructor Blank for Serialization of
 * {@link java.io.Serializable#Serializable() Serializable}.
 * and also for Json.
 * Managed by:
 * {@link EntityRegistry#EntityRegistry() EntityRegistry.createEntity()}.
 * 
 * @author Jack A. Leoanrd
 * @since 20th June 2025
 */
public abstract class Component implements java.io.Serializable {

    private int entity;

    /**
     * Assoicateed Intger Entity
     * 
     * @return Assoicateed Intger Entity
     */
    public int getEntity() {
        return entity;
    }

    /**
     * Is only sets by Component Pool
     * {@link ComponentPool#ComponentPool() ComponentPool()}.
     * {@link EntityRegistry#createEntity() EntityRegistry.createEntity()}.
     * 
     * @param entity
     */
    protected void setEntity(int entity) {
        this.entity = entity;
    }

    @AutoInstantiate
    transient protected Component requestComponent;

    /**
     * Gets called when be reset for reuse, by Component Pool
     * 
     * @param entityRegistry
     */
    public abstract void onReset(Registry registry);

    /**
     * Get called when entity is destroyed.
     * 
     * @param entityRegistry
     */
    // public abstract void onDestroy(EntityRegistry entityRegistry);

    /**
     * Gets called when instance is loaded from file.
     * 
     * @param entityRegistry
     */
    public abstract void onLoad(Registry registry);

}
