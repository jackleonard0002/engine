package engine.registry.render.buff.batch;

import engine.registry.Component;
import engine.registry.Registry;
import engine.registry.render.HasRenderer;

public final class BatchRenderComp extends Component implements HasRenderer<BatchRenderer> {

    transient private BatchRenderer batchRenderer;
    private BatchRenderConfig batchRenderConfig = new BatchRenderConfig();

    public BatchRenderConfig getBatchRenderConfig() {
        return batchRenderConfig;
    }

    @Override
    public void onReset(Registry registry) {
        batchRenderer = new BatchRenderer();
        batchRenderer.setEntity(getEntity());
        batchRenderer.reset(registry);
        batchRenderer.setRenderConfig(batchRenderConfig);
    }

    @Override
    public void onLoad(Registry registry) {
        batchRenderer = new BatchRenderer();
        batchRenderer.setEntity(getEntity());
        batchRenderer.reset(registry);
        batchRenderer.setRenderConfig(batchRenderConfig);
    }

    @Override
    public BatchRenderer getRenderer() {
        return batchRenderer;
    }

}
