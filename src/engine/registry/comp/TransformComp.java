package engine.registry.comp;

import engine.registry.Component;
import engine.registry.EntityRegistry;
import engine.registry.Registry;
import engine.util.Transform;

public class TransformComp extends Component {

    private Transform transform = new Transform();

    public Transform getTransform() {
        return transform;
    }

    public TransformComp() {
    }

    @Override
    public void onReset(Registry registry) {
        transform = new Transform();
    }

    @Override
    public void onLoad(Registry registry) {
        transform = new Transform();
    }

    @Override
    public void onUnload(Registry registry) {
        // TODO Auto-generated method stub
    }í
}