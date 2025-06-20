package engine.util;

import java.io.Serializable;

public class Vec2f implements Serializable {

    // DO NOT USE THESE CONSTANTS, THEY DO NOT WORK AND CAUSE GLITCHES OUT
    // OF THIS WORLD
    public static final Vec2f up = new Vec2f(0, 1);
    public static final Vec2f down = new Vec2f(0, -1);
    public static final Vec2f forward = new Vec2f(1, 0);
    public static final Vec2f back = new Vec2f(-1, 0);
    public static final Vec2f none = new Vec2f(0, 0);

    public static Vec2f allValues(float num) {
        return new Vec2f(num, num);
    }

    public static Vec2f negate(Vec2f arg0) {
        return new Vec2f(arg0.x * -1, arg0.y * -1);

    }

    public static Vec2f plus(Vec2f a, Vec2f b) {
        return new Vec2f(a.x + b.x, a.y + b.y);
    }

    public static Vec2f take(Vec2f a, Vec2f b) {
        return new Vec2f(a.x - b.x, a.y - b.y);
    }

    public static Vec2f times(Vec2f a, Vec2f b) {
        return new Vec2f(a.x * b.x, a.y * b.y);
    }

    public static Vec2f divide(Vec2f a, Vec2f b) {
        return new Vec2f(a.x / b.x, a.y / b.y);
    }

    public static boolean isEqual(Vec2f a, Vec2f b) {
        if (a.x == b.x && a.y == b.y) {
            return true;
        }
        return false;
    }

    public static Vec2f round(Vec2f a) {
        Logger.log("Runs");
        Vec2f arg0 = Vec2f.none;
        arg0.x = (float) Utilities.round(a.x);
        arg0.y = (float) Utilities.round(a.y);
        return arg0;
    }

    public static boolean isGreaterOrEqual(Vec2f a, Vec2f b) {
        if (a.x >= b.x &&
                a.y >= b.y) {
            return true;
        }
        return false;
    }

    public static double round(float value) {
        double scaleFactor = Math.pow(10, 2);
        return (Math.round(value * scaleFactor) / scaleFactor);
    }

    public static boolean isGreater(Vec2f a, Vec2f b) {
        if (a.x > b.x &&
                a.y > b.y) {
            return true;
        }
        return false;
    }

    public static float Angle(Vec2f from, Vec2f to) {
        return (float) Math.cos(((from.x * to.x) + (from.y * to.y) / (Math.sqrt(from.x * from.x + from.y * from.y))
                + (Math.sqrt(to.x * to.x + from.y * to.y))));

        /*
         * ┌ ax+by+cz ┐
         * Θ = cos │ ─────────────────────── │
         * └ √(a²+b²+c²)+√(x²+y²+z²) ┘
         */

    }

    public static Vec2f ClampMagnitude(Vec2f vector, float maxLength) {
        if (vector.magnitude > maxLength) {

        }
        /*
         * 
         * sqr(x²+y²)
         * 
         */

        return new Vec2f(vector.x, vector.y);
    }

    // Object
    public float x;
    public float y;
    protected float magnitude;

    public Vec2f plus(Vec2f add) {
        x += add.x;
        y += add.y;
        return new Vec2f(x, y);
    }

    public Vec2f take(Vec2f take) {
        x -= take.x;
        y -= take.y;
        return new Vec2f(x, y);
    }

    public Vec2f times(Vec2f take) {
        x *= take.x;
        y *= take.y;
        return new Vec2f(x, y);
    }

    public Vec2f divide(Vec2f divide) {
        x /= divide.x;
        y /= divide.y;
        return new Vec2f(x, y);
    }

    public void equal(Vec2f equal) {
        x = equal.x;
        y = equal.y;
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
        // this.magnitude = (float) Math.sqrt(x*x+y*y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
        this.magnitude = (float) Math.sqrt(x * x + y * y);
    }

    // public String toString() {
    // return x + "," + y;
    // }

    public float getValue(int index) {
        Float[] Position = { x, y };
        return Position[index];
    }

}
