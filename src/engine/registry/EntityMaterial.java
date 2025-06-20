package engine.registry;

import java.util.HashSet;
import java.util.Set;

public final class EntityMaterial {

    public static final int FLAG_DONT_SAVE = 1 << 1;
    public static final int FLAG_NOT_EDITABLE = 1 << 2;

    private HashSet<Component> components = new HashSet<>();
    private int flags = 0;

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(HashSet<Component> components) {
        this.components = components;
    }

}
