package engine.registry;

public interface HasProcess<T extends engine.registry.Process> {
    T getProcess();
}
