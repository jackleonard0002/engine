package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import engine.util.Logger;
import engine.util.Vec2f;

public abstract class Window extends JFrame {

    public static final Vec2f TETRIS_SCREEN = new Vec2f(512 + 16 + 256, 512 + 512 - 24);
    public static final Vec2f HALF_SCREEN = new Vec2f(1920 / 2, 1080 / 2);
    public static final Vec2f THREEQUARTER_SCREEN = new Vec2f(1920 * 0.75f, 1080 * 0.75f);
    public static final Vec2f FULL_SCREEN = new Vec2f(1920, 1080);
    public static final Vec2f TERMINAL_SCREEN = new Vec2f(1920 * 0.375f, 1080 / 2);

    private String name = "";
    private Canvas cover;

    private boolean isPaused = false;
    private boolean running = true;
    private long lastTime = System.currentTimeMillis();
    private int frameCount = 0;
    private int fps = 0;
    private long start_time;
    private int width, height;
    private double roundedMemoryInMB = 0;

    private boolean infoWindow = true;
    private boolean dispalyFPS = false;
    private boolean displayMU = false;
    private boolean enableRuntimeGCcall = false;
    private Color backColor = Color.WHITE;
    private boolean isFullscreen = false;

    private Window parent;

    public abstract void initialize();

    public abstract void suspend();

    public abstract void onTick(float deltaT);

    public abstract void onRender(Graphics2D g2d);

    public Window(String name, Vec2f size) {
        start_time = System.currentTimeMillis();
        this.name = name;
        this.width = (int) size.x;
        this.height = (int) size.y;
        setPreferredSize(new Dimension(this.width, this.height));
        setMinimumSize(new Dimension(this.width / 2, this.height / 2));
        setSize(new Dimension(this.width, this.height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(this.name);
        setResizable(true);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        // setLayout(null);
    }

    public void run() {
        this.cover = new Canvas();
        // cover.createBufferStrategy(2);
        // add(cover);

        // getContentPane().add(cover);

        getContentPane().add(cover, BorderLayout.CENTER);
        this.setVisible(true);

        long endTime = System.currentTimeMillis();
        long loadtime = (start_time - endTime) * -1;

        Logger.log(Logger.CYA, "Loadtime: " + loadtime + "ms");

        Runtime runtime = Runtime.getRuntime();
        Timer timer = new Timer();

        initialize();

        // revalidate(); // Needed to apply changes
        // repaint();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (enableRuntimeGCcall)
                    runtime.gc();
                long memoryInBytes = runtime.totalMemory() - runtime.freeMemory();
                double memoryInMB = memoryInBytes / (1024.0 * 1024.0);
                roundedMemoryInMB = Math.round(memoryInMB * 100.0) / 100.0;

            }
        }, 0, 2500); // Initial delay 0 ms, repeat every 500 ms

        if (infoWindow) {
            new Window("Info Window", Window.TERMINAL_SCREEN) {

                @Override
                public void initialize() {
                    this.setInfoWindow(false);
                    this.setDispalyFPS(true);
                    this.setDisplayMU(true);
                    this.setBackColor(Color.BLACK);
                    this.setLocation(50, 100);
                    setParent(Window.this);
                    Window.this.toFront();
                }

                @Override
                public void suspend() {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTick(float deltaT) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onRender(Graphics2D g2d) {
                    if (getParent() == null) {
                        System.out.println("is Null");
                        return;
                    }
                    g2d.setColor(Color.GREEN);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                    g2d.drawString("Main Window", 120, 20);
                    g2d.drawString("-----------", 120, 40);
                    g2d.drawString("" + getParent().getFps(), 120, 60);
                    g2d.drawString("" + getParent().getRoundedMemoryInMB() + "MB", 120, 80);
                }
            }.run();
        }

        // SwingUtilities.invokeLater(() -> {
        // toggleFullscreen();
        // });

        // Create the buffer strategy
        runGameLoop();
    }

    private void runGameLoop() {
        running = true;
        cover.createBufferStrategy(2);

        BufferStrategy bufferStrategy = cover.getBufferStrategy();
        // Start the rendering thread
        cover.setFocusable(true);

        Thread renderThread = new Thread(() -> {
            while (running) {
                if (!isPaused) {
                    frameCount++;
                    if (System.currentTimeMillis() - lastTime >= 1000) {
                        fps = frameCount;
                        frameCount = 0;
                        lastTime = System.currentTimeMillis();
                    }

                    onTick(0.5f); // Update logic only when not paused

                    do {
                        do {
                            Graphics g = bufferStrategy.getDrawGraphics();
                            internalRender((Graphics2D) g); // Render graphics
                            g.dispose();
                        } while (bufferStrategy.contentsRestored());
                        bufferStrategy.show();
                    } while (bufferStrategy.contentsLost());
                } else {

                    // Small sleep to prevent unnecessary CPU usage
                    try {
                        Thread.sleep(5); // Adjust timing as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            bufferStrategy.dispose();
        });
        renderThread.start();
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    public void internalRender(Graphics2D g2d) {
        g2d.setColor(backColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        onRender(g2d);

        if (dispalyFPS) {
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("" + fps, 10, 20);
        }
        if (displayMU) {
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("" + roundedMemoryInMB + "MB", 10, 40);
        }
    }

    public void toggleFullscreen() {

        setVisible(false);
        isPaused = true;
        dispose(); // Remove frame before modifying decorations
        // cover.getBufferStrategy().dispose();

        if (isFullscreen) {
            setUndecorated(false); // Restore decorations
            // device.setFullScreenWindow(null); // Exit fullscreen mode
            setExtendedState(JFrame.NORMAL);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true); // Remove window decorations
            // device.setFullScreenWindow(this); // Enter fullscreen mode
        }

        setVisible(true); // Show frame again
        isFullscreen = !isFullscreen;
        isPaused = false;
        // getCover().createBufferStrategy(2);

    }

    public boolean isInfoWindow() {
        return infoWindow;
    }

    public void setInfoWindow(boolean infoWindow) {
        this.infoWindow = infoWindow;
    }

    public boolean isDispalyFPS() {
        return dispalyFPS;
    }

    public void setDispalyFPS(boolean dispalyFPS) {
        this.dispalyFPS = dispalyFPS;
    }

    public boolean isDisplayMU() {
        return displayMU;
    }

    public void setDisplayMU(boolean displayMU) {
        this.displayMU = displayMU;
    }

    public Color getBackColor() {
        return backColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public Window getParent() {
        return parent;
    }

    public void setParent(Window parent) {
        this.parent = parent;
    }

    public int getFps() {
        return fps;
    }

    public double getRoundedMemoryInMB() {
        return roundedMemoryInMB;
    }

    public Canvas getCover() {
        return cover;
    }

}