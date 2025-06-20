package engine.util;

public class LoggerType {

    private String name = "";
    private boolean logPathOrName = false;
    private String color = "\u001B[32m";
    private boolean enabled = true;

    public LoggerType(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean islogPathOrName() {
        return logPathOrName;
    }

    public void setlogPathOrName(boolean logPathOrName) {
        this.logPathOrName = logPathOrName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
