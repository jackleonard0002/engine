package engine.registry.render.buff;

import engine.registry.render.RenderConfig;

public abstract class BufferedRenderConfig extends RenderConfig {
    protected boolean stretchImage = false;
    protected String imageID = "Big_Del_Button_G";

    protected BufferedRenderConfig() {

    }

    public boolean isStretchImage() {
        return stretchImage;
    }

    public void setStretchImage(boolean stretchImage) {
        if (this.stretchImage != stretchImage) {
            this.stretchImage = stretchImage;
            eventListener.triggerEvent(this);
        }
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        if (this.imageID != imageID) {
            this.imageID = imageID;
            eventListener.triggerEvent(this);
        }
    }
}
