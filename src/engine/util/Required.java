package engine.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Required<T> implements Serializable {
    private final Set<Class<? extends T>> required = new HashSet<>();

    public void addRequiredComponent(Class<? extends T> required) {
        this.required.add(required);
    }

    public void addRequiredComponents(Set<Class<? extends T>> required) {
        this.required.addAll(required);
    }

    public Set<Class<? extends T>> peekRequiredComponents() {
        return required;
    }

}