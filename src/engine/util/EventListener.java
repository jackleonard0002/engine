package engine.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EventListener<T> {
    private Set<Consumer<T>> listeners = new HashSet<>();

    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void triggerEvent(T event) {
        for (Consumer<T> listener : new HashSet<>(listeners)) {
            listener.accept(event);
        }
    }
}
