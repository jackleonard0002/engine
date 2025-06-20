package engine.util;

public class Timer {
    private static long startTime;
    private static long endTime;
    private static boolean running;

    public static void start() {
        Logger.log(Logger.CYA, "Timer Started");
        startTime = System.nanoTime();
        running = true;
    }

    public static void stop() {
        Logger.log(Logger.CYA, "Timer Stopped");
        endTime = System.nanoTime();
        running = false;
    }

    public static long getElapsedNanoseconds() {
        return running ? System.nanoTime() - startTime : endTime - startTime;
    }

    public static double getElapsedMilliseconds() {
        return getElapsedNanoseconds() / 1_000_000.0;
    }

    public static double getElapsedSeconds() {
        return getElapsedNanoseconds() / 1_000_000_000.0;
    }
}
