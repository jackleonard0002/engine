package engine.registry.comp.script;

import java.util.HashSet;

import engine.registry.Registry;
import engine.util.Logger;

public class ScriptableComp extends engine.registry.Component {

    private HashSet<Script> scripts = new HashSet<>();

    public HashSet<Script> getScripts() {
        return scripts;
    }

    @Override
    public void onReset(Registry registry) {
        // scripts.clear();
        scripts.forEach(s -> s.setEntity(getEntity()));
        for (Script script : new HashSet<>(scripts)) {
            script.onReset(registry);
        }
    }

    @Override
    public void onLoad(Registry registry) {
        Logger.log(Logger.EXOT, "getEntity(): " + getEntity());
        scripts.forEach(s -> s.setEntity(getEntity()));
        for (Script script : new HashSet<>(scripts)) {
            script.onLoad(registry);
        }
    }

    @Override
    public void onUnload(Registry registry) {
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
