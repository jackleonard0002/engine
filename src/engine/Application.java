package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import engine.registries.Component;
import engine.registries.Registry;
import engine.scene.Scene;
import engine.util.gfx.Assets;

public class Application {

    private static Application application;

    public static void initializeCore() {
        Assets.initializeImages();
    }

    public static Application createApplication(String... args) {
        application = new Application();
        return application;
    }

    public static void shutdownCore() {

    }

    public void initialize() {
        Scene.createScene();
        Registry<Component> components = new Registry<>();

    }

    public void run() {

        new Window("Engine", Window.HALF_SCREEN) {

            @Override
            public void initialize() {
                setInfoWindow(false);
                setDispalyFPS(true);
                setBackColor(Color.BLACK);
                setIconImage(Assets.getImage("icon"));
                Scene.setCover(getCover());
                Scene.getLiveScene().initialize();

                getCover().addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_T) {
                            // Scene.loadScene();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_F11) {
                            toggleFullscreen();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }

                });

            }

            @Override
            public void suspend() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTick(float deltaT) {

            }

            @Override
            public void onRender(Graphics2D g2d) {
                Scene.getLiveScene().onRender(g2d);
            }

        }.run();

    }
}
