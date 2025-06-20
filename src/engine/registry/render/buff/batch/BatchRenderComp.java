package engine.registry.render.buff.batch;

import engine.registry.Component;
import engine.registry.EntityRegistry;
import engine.registry.render.HasRenderer;

public final class BatchRenderComp extends Component implements HasRenderer<BatchRenderer> {

    transient private BatchRenderer batchRenderer;
    private BatchRenderConfig batchRenderConfig = new BatchRenderConfig();

    public BatchRenderConfig getBatchRenderConfig() {
        return batchRenderConfig;
    }

    @Override
    public void onReset(EntityRegistry entityRegistry) {
        batchRenderer = new BatchRenderer();
        batchRenderer.setEntity(getEntity());
        batchRenderer.reset(entityRegistry);
        batchRenderer.setRenderConfig(batchRenderConfig);
    }

    @Override
    public void onLoad(EntityRegistry entityRegistry) {
        batchRenderer = new BatchRenderer();
        batchRenderer.setEntity(getEntity());
        batchRenderer.reset(entityRegistry);
        batchRenderer.setRenderConfig(batchRenderConfig);
    }

    @Override
    public void onUnload(EntityRegistry entityRegistry) {
        // TODO Auto-generated method stub
    }

    @Override
    public BatchRenderer getRenderer() {
        return batchRenderer;
    }

}
