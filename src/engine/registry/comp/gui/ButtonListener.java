package engine.registry.comp.gui;

/**
 * Interface called from {@link ButtonComp}.
 * 
 */
public interface ButtonListener {
    /**
     * Is called when listener is activated.
     */
    public void onEnter();

    /**
     * Is called when listener is deactivated.
     */
    public void onExit();

    /**
     * Is called when hover on button
     */
    public void onHover();

    /**
     * Is called when leaving button area.
     * Not hoverin. Called onExit()
     */
    public void onAway();

    /**
     * Is called when over button and is mouse is pressed.
     */
    public void onHold();

    /**
     * is called when pressed and released over button
     */
    public void onPress();

    /**
     * is called when pressed and released over button
     */
    public void onAbort();
}