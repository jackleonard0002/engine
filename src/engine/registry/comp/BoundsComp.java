package engine.registry.comp;

import engine.registry.Component;
import engine.registry.Registry;
import engine.util.Bounds;

public class BoundsComp extends Component {

    private Bounds bounds = new Bounds();

    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void onReset(Registry registry) {
        // bounds = new Bounds();
    }

    @Override
    public void onLoad(Registry registry) {
        // bounds = new Bounds();
    }

    @Override
    public void onUnload(Registry registry) {
        // TODO Auto-generated method stub
    }
}
