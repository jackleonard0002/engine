package engine.registry.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import engine.registry.comp.BoundsComp;
import engine.registry.comp.Container;
import engine.registry.comp.TransformComp;
import engine.scene.AutoInstantiate;
import engine.util.Bounds;
import engine.util.ConsumerStack;
import engine.util.Logger;
import engine.util.Transform;
import engine.util.Vec2f;

public abstract class Renderer<T extends RenderConfig> extends engine.registry.Process {

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Maintains the aspect radio of Bounds relative to parent.
     * When enabled stretches to {@link #getBounds() getBounds()}
     */
    public static final int FLAG_MAINTAIN_RADIO_OF_PARENT = 1 << 0;

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Enables drawing of outline.
     */
    public static final int FLAG_DRAW_OUTLINE = 1 << 1;

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Enables {@link #getCoordinateOrgin() getCoordinateOrgin()}.
     */
    public static final int FLAG_ORGIN_CHANGE = 1 << 2;

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Enables Layout Grid
     */
    public static final int FLAG_LAYOUT_GRID = 1 << 3;

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Enables layout Grid spanning across different grids.
     */
    public static final int FLAG_LAYOUT_GRID_SPANNING = 1 << 4;

    /**
     * Flag for {@link #getFlags() getFlags()}.
     * Enables Parent Following. There are also flags
     * for enable each dimensions
     * 
     * @see
     *      {@link #FLAG_FOLLOW_X FLAG_FOLLOW_X},
     *      {@link #FLAG_FOLLOW_Y FLAG_FOLLOW_Y},
     *      {@link #FLAG_FOLLOW_WIDTH FLAG_FOLLOW_WIDTH},
     *      {@link #FLAG_FOLLOW_HEIGHT FLAG_FOLLOW_HEIGHT},
     *      {@link #FLAG_FOLLOW_ALL FLAG_FOLLOW_ALL},
     *      {@link #FLAG_FOLLOW_NONE FLAG_FOLLOW_NONE},
     *      {@link #FLAG_FOLLOW_POS_ONLY FLAG_FOLLOW_POS_ONLY},
     */
    public static final int FLAG_FOLLOWING = 1 << 5;
    public static final int FLAG_FOLLOW_X = 1 << 6;
    public static final int FLAG_FOLLOW_Y = 1 << 7;
    public static final int FLAG_FOLLOW_WIDTH = 1 << 8;
    public static final int FLAG_FOLLOW_HEIGHT = 1 << 9;

    public static final int FLAG_FOLLOW_ALL = FLAG_FOLLOW_X | FLAG_FOLLOW_Y | FLAG_FOLLOW_WIDTH | FLAG_FOLLOW_HEIGHT;
    public static final int FLAG_FOLLOW_NONE = ~(FLAG_FOLLOW_ALL);
    public static final int FLAG_FOLLOW_BUT_HEIGHT = FLAG_FOLLOW_X | FLAG_FOLLOW_Y | FLAG_FOLLOW_WIDTH;
    public static final int FLAG_FOLLOW_BUT_WIDTH = FLAG_FOLLOW_X | FLAG_FOLLOW_Y | FLAG_FOLLOW_HEIGHT;
    public static final int FLAG_FOLLOW_POS_ONLY = FLAG_FOLLOW_X | FLAG_FOLLOW_Y;

    public static final int FLAG_DEFAULT = FLAG_MAINTAIN_RADIO_OF_PARENT | FLAG_FOLLOWING | FLAG_FOLLOW_ALL
            | FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID;

    // private int flags = FLAG_DEFAULT;
    // private int layoutColNum = 2;
    // private int layoutRowNum = 1;
    // private int layoutCol = 1;
    // private int layoutRow = 0;
    @SuppressWarnings("unchecked")
    private T renderConfig = (T) new RenderConfig() {
    };

    @AutoInstantiate
    transient private Bounds bounds = new Bounds();
    @AutoInstantiate
    transient private Transform transform = new Transform();
    @AutoInstantiate
    transient protected final Bounds renderBounds = new Bounds();
    @AutoInstantiate
    transient private Collection<Integer> containerEntities;
    @AutoInstantiate
    transient private Renderer<?> parent;
    @AutoInstantiate
    transient private Container containerComp = null;

    public Renderer() {
        super();
        pushResetCallBack((entityRegistry) -> {
            renderCallBacks = new ConsumerStack<>();
            containerComp = entityRegistry.requestComponentFrom(getEntity(), Container.class, null);
            containerComp.getEventListener().addListener((cc) -> {
                Logger.log(cc.toString());
                containerEntities = cc.getChildren();
                interFun();
            });
            containerEntities = containerComp.getChildren();
            BoundsComp boundsComp = entityRegistry.requestComponentFrom(getEntity(), BoundsComp.class, null);
            bounds = boundsComp.getBounds();
            boundsComp.getBounds().getEventListener().addListener((bounds) -> {
                Logger.log("Bounds change");
                bounds = boundsComp.getBounds();
                interFun();
            });

            TransformComp transformComp = entityRegistry.requestComponentFrom(getEntity(), TransformComp.class, null);
            transformComp.getTransform().getEventListener().addListener((tra) -> {
                interFun();
            });
            // interFun();
        });
    }

    @AutoInstantiate
    transient private final Stack<Runnable> runnableStack = new Stack<>();

    protected Stack<Runnable> getRunnableStack() {
        return runnableStack;
    }

    public final void interFun() {
        // Logger.log("interFun change: ");
        // int[] entityIDs = null;
        // for (int entityID : entityIDs) {
        // Scene.getLiveScene().getEntity(entityID).getComponentInParent(RenderComp.class);
        // }
        if (containerEntities == null)
            return;

        for (int child : containerEntities) {
            HasRenderer<?> hasRenderer = containerComp.getRegistry().getComponentWithInterface(child,
                    HasRenderer.class);
            if (hasRenderer == null)
                continue;
            Renderer<?> renderer = hasRenderer.getRenderer();
            // Logger.log("GOT HERE");
            if (renderer == null)
                continue;
            renderer.interFun();
            renderer.superRenderBoundsUpdator(this);
        }
        superRenderBoundsUpdator(parent);
        for (Runnable runnable : runnableStack) {
            runnable.run();
        }
    }

    /// Render Callback ///

    @AutoInstantiate
    transient private ConsumerStack<Graphics2D> renderCallBacks = new ConsumerStack<>();

    public ConsumerStack<Graphics2D> getRenderCallBacks() {
        return renderCallBacks;
    }

    public void onRender(Graphics2D g2d) {
        // interFun();
        // Logger.log("Rendering in render");
        renderCallBacks.onAccept(g2d);

        if ((renderConfig.flags & FLAG_DRAW_OUTLINE) != 0) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));
            int x = (int) getRenderX();
            int y = (int) getRenderY();
            int width = (int) getRenderWidth();
            int height = (int) getRenderHeight();
            g2d.drawRect(Math.min(x, x + width), Math.min(y, y + height), Math.abs(width), Math.abs(height));
        }
    }

    public void superRenderBoundsUpdator(Renderer<?> parentRenderer) {
        float _x = bounds.getPosition().x;
        float _y = bounds.getPosition().y;
        float _width = bounds.getSize().x;
        float _height = bounds.getSize().y;

        float x = bounds.getPosition().x;
        float y = bounds.getPosition().y;
        float width = bounds.getSize().x;
        float height = bounds.getSize().y;

        // AffineTransform at0 = AffineTransform.getTranslateInstance(0, 0);
        // this.affineTransform = at0;
        if (parentRenderer != null && (renderConfig.flags & FLAG_FOLLOWING) != 0) {
            this.parent = parentRenderer;
            // Logger.log(Logger.EXOT, "parent running");
            float parentX = (float) parent.renderBounds.getPosition().x;
            float parentY = (float) parent.renderBounds.getPosition().y;
            float parentWidth = (float) parent.renderBounds.getSize().x;
            float parentHeight = (float) parent.renderBounds.getSize().y;

            float parentAdjustedX = parentX;
            float parentAdjustedY = parentY;
            float parentAdjustedWidth = parentWidth;
            float parentAdjustedHeight = parentHeight;
            if (((renderConfig.flags & FLAG_LAYOUT_GRID_SPANNING) != 0)) {
                int startRow = 1, startCol = 1;
                int spanRows = 1, spanCols = 1;

                parentAdjustedWidth = spanCols * (parentWidth / renderConfig.layoutColNum);
                parentAdjustedHeight = spanRows * (parentHeight / renderConfig.layoutRowNum);

                parentAdjustedX = parentX + (startCol * (parentWidth / renderConfig.layoutColNum));
                parentAdjustedY = parentY + (startRow * (parentHeight / renderConfig.layoutRowNum));
            } else if (((renderConfig.flags & FLAG_LAYOUT_GRID) != 0)) {
                // Logger.log("LAYOUT GRID");
                parentAdjustedWidth = parentWidth / renderConfig.layoutColNum;
                parentAdjustedHeight = parentHeight / renderConfig.layoutRowNum;

                parentAdjustedX = parentX + (renderConfig.layoutCol * parentAdjustedWidth);
                parentAdjustedY = parentY + (renderConfig.layoutRow * parentAdjustedHeight);

            }

            if (((renderConfig.flags & FLAG_MAINTAIN_RADIO_OF_PARENT) != 0)) {

                Rectangle child = new Rectangle((int) x, (int) y, (int) width, (int) height);
                Rectangle parent = new Rectangle((int) parentAdjustedX, (int) parentAdjustedY,
                        (int) parentAdjustedWidth, (int) parentAdjustedHeight);
                Rectangle newchild = fitRectangleInside(parent, child);

                parentAdjustedX = (float) newchild.getX();
                parentAdjustedY = (float) newchild.getY();
                parentAdjustedWidth = (float) newchild.getWidth();
                parentAdjustedHeight = (float) newchild.getHeight();
            }

            if ((renderConfig.flags & FLAG_ORGIN_CHANGE) != 0) {
                parentAdjustedWidth = (float) (_width * transform.getScale().x);
                parentAdjustedHeight = (float) (_height * transform.getScale().y);
                parentAdjustedX = parentX - (_x);
                parentAdjustedY = parentY - (_y);
                // Logger.LOG("Running here");
                parentAdjustedWidth = -parentAdjustedWidth;
                // at0.scale(-1, 1);
                parentAdjustedX = (parentAdjustedX + parentWidth);
            }

            if ((renderConfig.flags & FLAG_FOLLOW_X) != 0) {
                _x = parentAdjustedX;
            }
            if ((renderConfig.flags & FLAG_FOLLOW_Y) != 0) {
                _y = parentAdjustedY;
            }
            if ((renderConfig.flags & FLAG_FOLLOW_WIDTH) != 0) {
                _width = parentAdjustedWidth;
            }
            if ((renderConfig.flags & FLAG_FOLLOW_HEIGHT) != 0) {
                _height = parentAdjustedHeight;
            }
        } else if (((renderConfig.flags & FLAG_MAINTAIN_RADIO_OF_PARENT) != 0)) {
            // width = (float) (this.resolutionX * transform.getScale().x);
            // height = (float) (this.resolutionY * transform.getScale().y);

            float aspectRatio = _width / _height;

            if (aspectRatio > 1) {
                // Landscape orientation: Scale width based on height
                _width = (int) (_height * aspectRatio);
            } else {
                // Portrait or square: Scale height based on width
                _height = (int) (_width / aspectRatio);
            }
            _x = x + (_width - _width) / 2;
            _y = y + (_height - _height) / 2;
        }

        renderBounds.setPosition(new Vec2f(_x, _y));
        renderBounds.setSize(new Vec2f(_width, _height));

        if (containerEntities == null)
            return;
        for (int child : containerEntities) {
            HasRenderer<?> hasRenderer = containerComp.getRegistry().getComponentWithInterface(child,
                    HasRenderer.class);
            if (hasRenderer == null)
                continue;
            Renderer<?> renderer = hasRenderer.getRenderer();
            if (renderer == null)
                continue;
            // Logger.log("GOT HERE");
            renderer.interFun();
            renderer.superRenderBoundsUpdator(this);
        }
    }

    public static Rectangle fitRectangleInside(Rectangle parent, Rectangle child) {
        double parentAspect = parent.getWidth() / parent.getHeight();
        double childAspect = child.getWidth() / child.getHeight();

        double scaleFactor = (childAspect > parentAspect)
                ? parent.getWidth() / child.getWidth()
                : parent.getHeight() / child.getHeight();

        int newWidth = (int) (child.getWidth() * scaleFactor);
        int newHeight = (int) (child.getHeight() * scaleFactor);

        int newX = parent.x + (parent.width - newWidth) / 2;
        int newY = parent.y + (parent.height - newHeight) / 2;

        return new Rectangle(newX, newY, newWidth, newHeight);
    }

    public float getRenderX() {
        return renderBounds.getPosition().x;
    }

    public float getRenderY() {
        return renderBounds.getPosition().y;
    }

    public float getRenderWidth() {
        return renderBounds.getSize().x;
    }

    public float getRenderHeight() {
        return renderBounds.getSize().y;
    }

    public Set<Integer> getContainerEntities() {
        return new HashSet<>(containerEntities);
    }

    public T getRenderConfig() {
        return renderConfig;
    }

    public void setRenderConfig(T renderConfig) {
        this.renderConfig = renderConfig;
        renderConfig.createEventListener();
        renderConfig.addListener((x) -> {
            interFun();
        });
        // interFun();
    }

}
