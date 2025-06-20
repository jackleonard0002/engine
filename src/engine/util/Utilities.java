package engine.util;

public class Utilities {

    public static float clamp(float var, float min, float max) {
        if (var > max)
            return var = max;
        else if (var < min)
            return var = min;
        else
            return var;
    }

    public static float Approuch(float goal, float current, float dt) {
        float difference = goal - current;
        if (difference > dt)
            return current + dt;
        if (difference < -dt)
            return current - dt;
        return goal;
    }

    public static float lerp(float point1, float point2, float d) {
        return point1 + d * (point2 - point1);
    }

    public static Vec2f rotate_point(float cx, float cy, float angle, float px, float py) {
        float absangl = Math.abs(angle);
        float s = (float) Math.sin(Math.toRadians(absangl));
        float c = (float) Math.cos(Math.toRadians(absangl));

        // translate point back to origin:
        px -= cx;
        py -= cy;

        // rotate point
        float xnew;
        float ynew;
        if (angle > 0) {
            xnew = px * c - py * s;
            ynew = px * s + py * c;
        } else {
            xnew = px * c + py * s;
            ynew = -px * s + py * c;
        }

        // translate point back:
        px = xnew + cx;
        py = ynew + cy;
        return new Vec2f(px, py);
    }

    public static Vec2f scale_point(Vec2f gameobjectpos, Vec2f camerascale, Vec2f windowscale) {

        // window & height and width what about them?

        float new_posx = (gameobjectpos.x * camerascale.x * windowscale.x);
        float new_posy = (gameobjectpos.y * camerascale.y * windowscale.y);

        // float new_posx =
        // (gameobjectpos.x*camerascale.x*windowscale.x)+((rotpoint.x)*windowscale.x);
        // float new_posy =
        // (gameobjectpos.y*camerascale.y*windowscale.y)+((rotpoint.y)*windowscale.y);

        // System.out.println("camerascale.x: "+camerascale.x);
        // System.out.println("camerascale.y: "+camerascale.y);
        //
        // System.out.println("windowscale.x: "+windowscale.x);
        // System.out.println("windowscale.y: "+windowscale.y);

        return new Vec2f(new_posx, new_posy);
    }

    public Vec2f scale_point(Vec2f offest, Vec2f windowsize, Vec2f windowscale, Vec2f position, Vec2f scale,
            float width, float height) {

        // A very very difficult fuction to write!!!!!
        // I want it to zoom into the camera's gameobject.
        // One step at a time.

        // gameobject transform position 200,200
        // camera offset scale 2,2
        // camera gameobject position 200,200

        // It changes all the final render transforms by the camera scale.
        // gameobject

        // float new_posx = (position.x*scale.x);
        // float new_posy = (position.y*scale.y);

        float new_posx = (position.x) * scale.x;
        float new_posy = (position.y) * scale.y;

        // to

        System.out.println("windowsize.x: " + windowsize.x);
        System.out.println("windowsize.y: " + windowsize.y);

        // new_posx = new_posx+(windowsize.x/2)/windowscale.x; // 920
        // new_posy = new_posy+(windowsize.y/2)/windowscale.y; // 540

        // new_posx = new_posx+((1920)/2 )/windowscale.x;
        // new_posy = new_posy+((1080)/2)/windowscale.y;

        // new_posx = new_posx+((640)/2 )/centrepos.x;
        // new_posy = new_posy+((640 / 12 * 9)/2)/centrepos.y;
        //
        // 16:: 0 16 32 48 64 72 84 96
        // 32:: -32 0 32 64 96
        // dd:: -32 -16 0 16 32 48

        // float new_posx = position.x-((width/2)*scale.x);
        // float new_posy = position.y-((height/2)*scale.y);

        return new Vec2f(new_posx, new_posy);

    }

    public static String getCCAM() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // Index 2 in the stack trace should be the caller of the method that called
        // getCallingClassAndMethodName
        StackTraceElement element = stackTrace[3];
        return element.getClassName() + "#" + element.getMethodName();
    }

    public static String geCMAC() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // Index 2: Current method (getClassMethodAndCaller)
        // Index 3: The method that called the current method
        StackTraceElement currentMethod = stackTrace[2];
        StackTraceElement callingMethod = stackTrace[3];
        return "Current Method: " + currentMethod.getClassName() + "#" + currentMethod.getMethodName() +
                ", Called From: " + callingMethod.getClassName() + "#" + callingMethod.getMethodName();
    }

    public static double round(float value) {
        double scaleFactor = Math.pow(10, 2);
        return (Math.round(value * scaleFactor) / scaleFactor);
    }

}
