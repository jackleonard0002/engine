package engine.util;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus implements Serializable {
    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add((Consumer<Object>) listener);
    }

    public <T> void publish(T event) {
        CopyOnWriteArrayList<Consumer<Object>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Consumer<Object> listener : eventListeners) {
                listener.accept(event);
            }
        }
    }

}