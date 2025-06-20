package engine.registry.render;

public interface HasRenderer<T extends Renderer<?>> {
    T getRenderer();
}
