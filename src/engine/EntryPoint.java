package engine;

/**
 * Game Engine.
 * 
 * @author Jack A.leonard
 * @since 17th June 2025
 */
public class EntryPoint {

    public static boolean continueApplicationRunning = false;

    public static void main(String... args) {

        do {
            engine.Application.initializeCore();
            engine.Application app = engine.Application.createApplication(args);
            app.initialize();
            app.run();
            engine.Application.shutdownCore();
        } while (continueApplicationRunning);

    }
}
