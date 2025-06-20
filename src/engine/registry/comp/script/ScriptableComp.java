package engine.registry.comp.script;

import java.util.HashSet;

import engine.registry.EntityRegistry;
import engine.util.Logger;

public class ScriptableComp extends engine.registry.Component {

    private HashSet<Script> scripts = new HashSet<>();

    public HashSet<Script> getScripts() {
        return scripts;
    }

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        // scripts.clear();
        scripts.forEach(s -> s.setEntity(getEntity()));
        for (Script script : new HashSet<>(scripts)) {
            script.onReset(entityRegistry);
        }
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        Logger.log(Logger.EXOT, "getEntity(): " + getEntity());
        scripts.forEach(s -> s.setEntity(getEntity()));
        for (Script script : new HashSet<>(scripts)) {
            script.onLoad(entityRegistry);
        }
    }

    @Override
    public void onUnload(EntityRegistry entityRegistry) {
        // TODO Auto-generated method stub
    }

    public void onTick(float deltaT) {
        scripts.forEach(s -> s.onTick(deltaT));
    }

    public void addScript(Script script) {
        script.setEntity(getEntity());
        scripts.add(script);
    }

    public void removeScript(Script script) {
        scripts.remove(script);
    }

}
