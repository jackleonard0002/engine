package engine.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static boolean LOG_PATH = false;
    public static boolean LOG_TIMESTAM = false;
    public static boolean LOG_TIMESTAM_ONLY = false;
    public static String IS_CONTAINED_IN_PATH = "";

    public static boolean ENABLE_MESS = true;
    public static boolean ENABLE_WARM = true;
    public static boolean ENABLE_ERR = true;

    public static final String MESS = "\u001B[32m";
    public static final String WARM = "\u001B[33m";
    public static final String ERR = "\u001B[31m";

    public static LoggerType EXOT;
    public static LoggerType DEE;
    public static LoggerType CYA;

    static {
        EXOT = new LoggerType("EXOT");
        EXOT.setColor(Constants.ANSI_PURPLE);

        DEE = new LoggerType("DEE");
        DEE.setColor(Constants.ANSI_BLACK);

        CYA = new LoggerType("CYA");
        CYA.setColor(Constants.ANSI_GREEN);
        CYA.setlogPathOrName(false);
    }

    private static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");

    public enum LOG_TYPE {
        MESS,
        WARM,
        ERR,
    }

    public static void log(String... args) {
        String arg1 = "";
        interal_log(arg1, "", LOG_PATH, args);
    }

    public static void log(LoggerType type, String... args) {
        String arg1 = "";
        if (!type.isEnabled())
            return;
        arg1 += type.getColor();
        interal_log(arg1, type.getName(), type.islogPathOrName(), args);
    }

    public static void log(LOG_TYPE type, String... args) {
        String arg1 = "";
        String log_name = "MESS";

        if (type == LOG_TYPE.MESS && !ENABLE_MESS) {
            return;
        }
        if (type == LOG_TYPE.WARM && !ENABLE_WARM) {
            return;
        }
        if (type == LOG_TYPE.ERR && !ENABLE_ERR) {
            return;
        }
        switch (type) {
            case MESS:
                arg1 += MESS;
                log_name += "MESS";
                break;
            case WARM:
                arg1 += WARM;
                log_name += "WARM";
                break;
            case ERR:
                arg1 += ERR;
                log_name += "ERR";
                break;
        }
        interal_log(arg1, log_name, LOG_PATH, args);
    }

    private static void interal_log(String arg1, String log_name, boolean logPath, String... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement currentMethod = stackTrace[3];
        boolean isInPath = currentMethod.getClassName().contains(IS_CONTAINED_IN_PATH);
        if (!isInPath) {
            return;
        }
        if (!LOG_TIMESTAM_ONLY) {
            arg1 += "[";
            if (logPath) {
                arg1 += currentMethod.getClassName();
                ;
                arg1 += "=>";
                arg1 += currentMethod.getMethodName();
            } else {
                arg1 += log_name;
            }
            arg1 += "]";
        }
        if (LOG_TIMESTAM) {
            arg1 += "[";
            arg1 += LocalTime.now().format(myFormatObj);
            arg1 += "] ";
        }
        for (String arg : args)
            arg1 += arg;
        arg1 += Constants.ANSI_RESET;
        System.out.println(arg1);
    }

    private boolean enabled = true;
    private Class<?> clazz;

    public Logger(Class<?> clazz, boolean enabled) {
        this.clazz = clazz;
        this.enabled = enabled;
    }

    public void iLog(String... args) {
        if (enabled) {
            String arg1 = "";
            interal_log(arg1, "", LOG_PATH, "[" + clazz.getSimpleName() + "]" + args);
        }
    }

    public void iLog(LoggerType type, String... args) {
        if (enabled) {
            log(type, "[" + clazz.getSimpleName() + "]" + args);
        }

    }

    public void iLog(LOG_TYPE type, String... args) {
        if (enabled) {
            log(type, "[" + clazz.getSimpleName() + "]" + args);
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
