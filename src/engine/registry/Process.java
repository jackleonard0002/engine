package engine.registry;

import java.util.function.Consumer;

import engine.util.ConsumerStack;

public class Process {

    private int entity;

    transient private ConsumerStack<EntityRegistry> resetStack = new ConsumerStack<>();

    transient protected Component requestComponent;

    protected Process() {
        resetStack = new ConsumerStack<>();
    }

    protected void pushResetCallBack(Consumer<EntityRegistry> resetConsumer) {
        resetStack.pushToStack(resetConsumer);
    }

    public void reset(EntityRegistry entityRegistry) {
        if (resetStack == null)
            resetStack = new ConsumerStack<EntityRegistry>();
        resetStack.onAccept(entityRegistry);
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }
}
