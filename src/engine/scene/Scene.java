package engine.scene;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import engine.registry.Component;
import engine.registry.ComponentAdapter;
import engine.registry.Registry;
import engine.registry.comp.BoundsComp;
import engine.registry.comp.Container;
import engine.registry.comp.KeyInputComp;
import engine.registry.comp.MouseInputComp;
import engine.registry.comp.gui.ButtonComp;
import engine.registry.comp.gui.panel.PanelCoreComp;
import engine.registry.comp.script.Script;
import engine.registry.comp.script.ScriptAdapter;
import engine.registry.comp.script.ScriptableComp;
import engine.registry.render.HasRenderer;
import engine.registry.render.RenderConfig;
import engine.registry.render.Renderer;
import engine.registry.render.buff.batch.BatchRenderComp;
import engine.registry.render.buff.batch.RenderBatch;
import engine.registry.render.buff.image.ImageComp;
import engine.registry.render.buff.image.ImageRenderer;
import engine.registry.render.rect.RectComp;
import engine.registry.render.rect.RectRenderer;
import engine.scene.panel.PanelCoreScript;
import engine.util.Logger;
import engine.util.Timer;
import engine.util.Vec2f;

public class Scene implements Serializable {

    private static Scene liveScene;
    transient private static Canvas cover;

    public static Canvas getCover() {
        return cover;
    }

    public static void setCover(Canvas cover) {
        Scene.cover = cover;
    }

    public static Scene getLiveScene() {
        return liveScene;
    }

    public static Scene createScene() {
        liveScene = new Scene();
        return liveScene;
    }

    public static void writeSceneToBIN(String filepath) {
        Logger.log(Logger.CYA, " (W)Began to write " + filepath);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(liveScene);
            Logger.log(Logger.CYA, " (W)Sucessfully Wrote to " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSceneFromBIN(String filepath) {
        Logger.log(Logger.CYA, " (R) Began to read " + filepath);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath))) {
            Scene scene = (Scene) in.readObject();
            ApplyDefaultValues.AutoInit(scene);
            swapScenes(scene);
            Logger.log(Logger.CYA, " (R) Sucessfully Read from " + filepath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeSceneToJSON(String filepath) {
        Logger.log(Logger.CYA, " (R) Began to read " + filepath);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        builder.registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(RenderConfig.class, new RenderConfigAdapter());
        // .registerTypeAdapter(Script.class, new ScriptAdapter(gson))
        // .registerTypeAdapter(Color.class, new ColorTypeAdapter())
        // gson = builder.setPrettyPrinting().create();
        // builder.registerTypeAdapter(ScriptableComp.class, new
        // ScriptableComponentAdapter());
        gson = builder.setPrettyPrinting().create();
        builder.registerTypeAdapter(Script.class, new ScriptAdapter(gson));
        gson = builder.setPrettyPrinting().enableComplexMapKeySerialization().create();
        try (FileWriter writer = new FileWriter(filepath)) {
            liveScene.registry.unloadALLComponents();
            gson.toJson(liveScene, writer);
            Logger.log(Logger.CYA, " (W) Sucessfully Wrote to " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene readSceneFromJSON(String filepath) {
        Logger.log(Logger.CYA, " (R) Began to read " + filepath);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        builder.registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(RenderConfig.class, new RenderConfigAdapter());
        // .registerTypeAdapter(Script.class, new ScriptAdapter(gson))
        // .registerTypeAdapter(Color.class, new ColorTypeAdapter())
        // gson = builder.setPrettyPrinting().create();
        // builder.registerTypeAdapter(ScriptableComp.class, new
        // ScriptableComponentAdapter());
        gson = builder.setPrettyPrinting().create();
        builder.registerTypeAdapter(Script.class, new ScriptAdapter(gson));
        gson = builder.setPrettyPrinting().enableComplexMapKeySerialization().create();
        try (FileReader reader = new FileReader(filepath)) {
            Scene scene = gson.fromJson(reader, Scene.class);
            Logger.log(Logger.CYA, " (R) Sucessfully Read from " + filepath);
            swapScenes(scene);
            return scene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void swapScenes(Scene scene) {
        scene.registry.loadALLComponents();
        // liveScene.isRender = false;s
        BoundsComp rootBounds = scene.registry.addComponent(1, BoundsComp.class);
        // br.setImageID("blue");
        ImageComp br = scene.registry.requestComponentFrom(1, ImageComp.class, null);
        br.getRenderer().getRenderConfig().setImageID("blue");

        getCover().addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Logger.log("Window Resized");
                Canvas cover = getCover();
                Point point = cover.getLocation();
                rootBounds.getBounds().setPosition(new Vec2f((float) point.getX(), (float) point.getY()));
                rootBounds.getBounds().setSize(new Vec2f((float) cover.getWidth(), (float) cover.getHeight()));
                Logger.log("Width: " + rootBounds.getBounds().getSize().x);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub
            }

        });
        getCover().addMouseMotionListener(scene.registry.requestComponentFrom(1, MouseInputComp.class, null));
        getCover().addKeyListener(scene.registry.requestComponentFrom(1, KeyInputComp.class, null));
        liveScene = scene;
        // liveScene.isRender = true;
    }

    private Registry registry = new Registry();
    private HashMap<String, Script> scriptRegistry = new HashMap<>();
    transient private int rootEntity;
    private boolean isRender = true;

    public void initialize() {
        scriptRegistry.put("PanelScript", new PanelCoreScript());
        rootEntity = registry.createEntity();
        registry.addComponent(rootEntity, BoundsComp.class);
        registry.addComponent(rootEntity, MouseInputComp.class);
        registry.addComponent(rootEntity, KeyInputComp.class);
        ImageComp br = registry.addComponent(rootEntity, ImageComp.class);
        ImageRenderer ir = br.getRenderer();
        ir.getRenderConfig().setImageID("blue");
        ir.getRenderConfig().setStretchImage(true);
        ir.getRenderConfig()
                .setFlags(Renderer.FLAG_FOLLOWING |
                        Renderer.FLAG_FOLLOW_ALL | Renderer.FLAG_LAYOUT_GRID);

        int button = registry.createEntity();
        registry.addComponent(button, ButtonComp.class);
        ImageComp bufferedRenderComp2 = registry.getComponent(button, ImageComp.class);
        ImageRenderer renderer1 = bufferedRenderComp2.getRenderer();
        RenderConfig renderConfig1 = renderer1.getRenderConfig();
        bufferedRenderComp2.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        // rootContainer.addChild(button);
        renderConfig1.setLayoutColNum(6);
        renderConfig1.setLayoutRowNum(6);
        renderConfig1.setLayoutCol(5);
        renderConfig1.setLayoutRow(0);

        int button2 = registry.createEntity();
        registry.addComponent(button2, ButtonComp.class);
        ImageComp bufferedRenderComp3 = registry.getComponent(button2, ImageComp.class);
        ImageRenderer renderer3 = bufferedRenderComp3.getRenderer();
        RenderConfig renderConfig3 = renderer3.getRenderConfig();
        bufferedRenderComp2.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        renderConfig3.setLayoutColNum(6);
        renderConfig3.setLayoutRowNum(6);
        renderConfig3.setLayoutCol(4);
        renderConfig3.setLayoutRow(0);

        int button3 = registry.createEntity();
        registry.addComponent(button3, ButtonComp.class);
        ImageComp bufferedRenderComp4 = registry.getComponent(button3, ImageComp.class);
        ImageRenderer renderer4 = bufferedRenderComp4.getRenderer();
        RenderConfig renderConfig4 = renderer4.getRenderConfig();
        bufferedRenderComp4.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        renderConfig4.setLayoutColNum(6);
        renderConfig4.setLayoutRowNum(6);
        renderConfig4.setLayoutCol(3);
        renderConfig4.setLayoutRow(0);

        int panel = registry.createEntity();
        RectComp rect = registry.addComponent(panel, RectComp.class);
        RectRenderer renderer = rect.getRenderer();
        RenderConfig renderConfig = renderer.getRenderConfig();
        renderConfig.setFlags(Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                | Renderer.FLAG_LAYOUT_GRID);
        registry.addComponent(panel, MouseInputComp.class);
        // entityRegistry.addComponent(panel, PanelKeyInputComp.class);
        // ScriptableComp scriptableComp = entityRegistry.addComponent(panel,
        // ScriptableComp.class);
        // scriptableComp.addScript(new PanelCoreScript());
        renderConfig.setLayoutColNum(3);
        renderConfig.setLayoutRowNum(3);
        renderConfig.setLayoutCol(1);
        renderConfig.setLayoutRow(1);
        // Container panelContainer = entityRegistry.getComponent(panel,
        // Container.class);
        // rootContainer.addChild(panel);
        // panelContainer.addChild(button);
        // panelContainer.addChild(button2);
        // panelContainer.addChild(button3);
        // int panelEntiy = createPanel();
        // rootContainer.addChild(panelEntiy);

        int panelCore = registry.createEntity();
        ScriptableComp coreScriptable = registry.addComponent(panelCore, ScriptableComp.class);
        coreScriptable.addScript(new PanelCoreScript());

        Container rootContainer = registry.getComponent(rootEntity, Container.class);
        // rootContainer.addChild(panelCore);

        int batchRenderTest = registry.createEntity();
        BatchRenderComp batchRenderComp = registry.addComponent(batchRenderTest, BatchRenderComp.class);
        batchRenderComp.getBatchRenderConfig().putRenderBatch(new Vec2f(1, 1), new RenderBatch("Big_Del_Button_G"));
        batchRenderComp.getBatchRenderConfig().putRenderBatch(new Vec2f(2, 1), new RenderBatch("Big_Del_Button_G"));
        batchRenderComp.getBatchRenderConfig().putRenderBatch(new Vec2f(2, 2), new RenderBatch("Big_Del_Button_G"));
        batchRenderComp.getRenderer().bake();

        int panelTest = registry.createEntity();
        registry.addComponent(panelTest, PanelCoreComp.class);

        rootContainer.putChild("batchRenderTest", batchRenderTest);
        rootContainer.putChild("panelCoreComp", panelTest);

        Timer.start();
        writeSceneToJSON("res/scene0.json");
        readSceneFromJSON("res/scene0.json");
        writeSceneToJSON("res/scene1.json");
        writeSceneToBIN("res/scene2.bin");
        readSceneFromBIN("res/scene2.bin");
        writeSceneToJSON("res/scene3.json");
        Timer.stop();
        Logger.log(Logger.EXOT, "######### Loading Took " + Timer.getElapsedMilliseconds() + " ms #########");
        // readSceneFromJSON("res/scene3.json");
    }

    public void onRender(Graphics2D g2d) {
        if (isRender) {
            onRenderEntityTree(g2d, registry, 1, 0);
        }
    }

    private void onRenderEntityTree(Graphics2D g2d, Registry entityRegistry, int entity, int depth) {
        // Logger.log("Render Tree Depth: " + ++depth);
        HasRenderer<?> hasRenderer = entityRegistry.getComponentWithInterface(entity, HasRenderer.class);
        if (hasRenderer == null)
            return;
        // Logger.log("GEts here: ");
        Renderer<?> renderer = hasRenderer.getRenderer();
        renderer.onRender(g2d);
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0); // 40, 100
        for (int child : renderer.getContainerEntities()) {
            onRenderEntityTree(g2d, entityRegistry, child, depth);
            g2d.setTransform(at);
        }
        g2d.setTransform(at);
    }

    public Registry getRegistry() {
        return registry;
    }

    public int createPanel() {
        int button = registry.createEntity();
        registry.addComponent(button, ButtonComp.class);
        ImageComp bufferedRenderComp2 = registry.getComponent(button, ImageComp.class);
        ImageRenderer renderer1 = bufferedRenderComp2.getRenderer();
        RenderConfig renderConfig1 = renderer1.getRenderConfig();
        bufferedRenderComp2.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        // rootContainer.addChild(button);
        renderConfig1.setLayoutColNum(6);
        renderConfig1.setLayoutRowNum(6);
        renderConfig1.setLayoutCol(5);
        renderConfig1.setLayoutRow(0);

        int button2 = registry.createEntity();
        registry.addComponent(button2, ButtonComp.class);
        ImageComp bufferedRenderComp3 = registry.getComponent(button2, ImageComp.class);
        ImageRenderer renderer3 = bufferedRenderComp3.getRenderer();
        RenderConfig renderConfig3 = renderer3.getRenderConfig();
        bufferedRenderComp2.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        renderConfig3.setLayoutColNum(6);
        renderConfig3.setLayoutRowNum(6);
        renderConfig3.setLayoutCol(4);
        renderConfig3.setLayoutRow(0);

        int button3 = registry.createEntity();
        registry.addComponent(button3, ButtonComp.class);
        ImageComp bufferedRenderComp4 = registry.getComponent(button3, ImageComp.class);
        ImageRenderer renderer4 = bufferedRenderComp4.getRenderer();
        RenderConfig renderConfig4 = renderer4.getRenderConfig();
        bufferedRenderComp4.getRenderer().getRenderConfig().setFlags(
                Renderer.FLAG_MAINTAIN_RADIO_OF_PARENT | Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                        | Renderer.FLAG_DRAW_OUTLINE | Renderer.FLAG_LAYOUT_GRID);
        renderConfig4.setLayoutColNum(6);
        renderConfig4.setLayoutRowNum(6);
        renderConfig4.setLayoutCol(3);
        renderConfig4.setLayoutRow(0);

        int panel = registry.createEntity();
        RectComp rect = registry.addComponent(panel, RectComp.class);
        RectRenderer renderer = rect.getRenderer();
        RenderConfig renderConfig = renderer.getRenderConfig();
        renderConfig.setFlags(Renderer.FLAG_FOLLOWING | Renderer.FLAG_FOLLOW_ALL
                | Renderer.FLAG_LAYOUT_GRID);
        registry.addComponent(panel, MouseInputComp.class);
        // entityRegistry.addComponent(panel, PanelKeyInputComp.class);
        ScriptableComp scriptableComp = registry.addComponent(panel, ScriptableComp.class);
        scriptableComp.addScript(new PanelCoreScript());
        renderConfig.setLayoutColNum(3);
        renderConfig.setLayoutRowNum(3);
        renderConfig.setLayoutCol(1);
        renderConfig.setLayoutRow(1);

        // ButtonComp panelButton = entityRegistry.addComponent(panel,
        // ButtonComp.class);

        // Container panelContainer = entityRegistry.getComponent(panel,
        // Container.class);
        // panelContainer.addChild(button);
        // panelContainer.addChild(button2);
        // panelContainer.addChild(button3);

        return panel;
    }
}
