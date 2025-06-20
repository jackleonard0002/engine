package engine.util.gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import engine.util.Logger;

public class Assets {

    public static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
    public static Map<String, BufferedImage> cachedImages = new HashMap<String, BufferedImage>();
    public static final BufferedImage NULL_IMAGE = createNullImage();

    public static BufferedImage createNullImage() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageg2d = image.createGraphics();
        imageg2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        imageg2d.setColor(Color.MAGENTA);
        imageg2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        imageg2d.setColor(Color.BLACK);
        imageg2d.setStroke(new BasicStroke(5));
        imageg2d.drawLine(0, 0, image.getWidth(), image.getHeight());
        imageg2d.drawLine(0, image.getWidth(), image.getHeight(), 0);
        imageg2d.dispose();
        return image;
    }

    public static BufferedImage getCachedImage(String key) {
        if (cachedImages.containsKey(key)) {
            return cachedImages.get(key);
        } else {
            // System.out.println("NULL");
            return null;
        }
    }

    public static void putCachedImage(String imageID, BufferedImage image) {
        cachedImages.put(imageID, image);
    }

    public static BufferedImage getImage(String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        } else {
            // System.out.println("NULL");
            return null;
        }
    }

    public static boolean imagesExists(String[] keys) {
        for (String key : keys) {
            if (!images.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public static void initializeImages() {
        // SpriteSheet blocksshest3 = null;
        try {
            SpriteSheet blockssheet3 = new SpriteSheet(ImageLoader.loadImage("res/Viewscreen Elements.png"));
            images.put("Min Button G", blockssheet3.crop(34, 34, 32, 32));
            images.put("Min Button", blockssheet3.crop(34, 1, 32, 32));
            images.put("Max Button", blockssheet3.crop(1, 1, 32, 32));
            images.put("Max Button G", blockssheet3.crop(1, 34, 32, 32));
            images.put("Del Button", blockssheet3.crop(67, 1, 32, 32));
            images.put("Del Button G", blockssheet3.crop(67, 34, 32, 32));

            // // Viewscreen BIG BUTTONS

            images.put("Big_Min_Button", blockssheet3.crop(2, 65, 48, 32));
            images.put("Big_Min_Button_G", blockssheet3.crop(2, 96, 48, 32));
            images.put("Big_Max_Button", blockssheet3.crop(53, 65, 48, 32));
            images.put("Big_Max_Button_G", blockssheet3.crop(53, 96, 48, 32));
            images.put("Big_Del_Button", blockssheet3.crop(104, 65, 48, 32));
            images.put("Big_Del_Button_G", blockssheet3.crop(104, 96, 48, 32));

            images.put("Big_Del_Button H", blockssheet3.crop(54, 130, 92, 60));

            images.put("Blank_Yellow", blockssheet3.crop(206, 66, 30, 30));
            images.put("Blank_Grey", blockssheet3.crop(206, 97, 10, 10));

        } catch (IOException e) {
            Logger.log(Logger.ERR, "Image not loaded");
            e.printStackTrace();
        }

        int size = 15;
        SpriteSheet LETTERS = null;
        try {
            LETTERS = new SpriteSheet(ImageLoader.loadImage("res/letters.png"));
            String n = "Letter ";
            String alpa = "abcdefghijklmnopqrstuvwxyz";
            String CAPalpa = alpa.toUpperCase();

            System.out.println("CAPalpa: " + CAPalpa);

            for (int i = 0; i < alpa.length() - 1; i++) {
                // System.out.println("III:: "+n+alpa.charAt(i));
                // System.out.println("II:: "+i+" "+(size*(i)));
                images.put(n + alpa.charAt(i), LETTERS.crop((size * (i)), 31, 16, 16));
            }

            images.put(n + "z", LETTERS.crop(size * 25, 31, 15, 15));

            for (int i = 0; i < CAPalpa.length() - 1; i++) {
                images.put(n + CAPalpa.charAt(i), LETTERS.crop((size * (i)), 8, 16, 16));
            }

            images.put(n + "UNDERSCORE", LETTERS.crop(270, 55, 15, 15));
            images.put(n + "SPACE", LETTERS.crop(315, 150, 15, 15));
            images.put(n + "POLL", LETTERS.crop(260, 78, 14, 16));

            // images.put(n+"a", LETTERS.crop(0, 31, 16, 16));
            // images.put(n+"b", LETTERS.crop(15*1, 31, 16, 16));
            // images.put(n+"c", LETTERS.crop(15*2, 31, 16, 16));
            // images.put(n+"d", LETTERS.crop(15*3, 31, 16, 16));
            // images.put(n+"e", LETTERS.crop(15*4, 31, 16, 16));
            // images.put(n+"f", LETTERS.crop(15*5, 31, 16, 16));

        } catch (IOException e) {
            Logger.log(Logger.ERR, "image not loaded");
            e.printStackTrace();
        }
        // icon logo.png
        SpriteSheet icon_logo;
        try {
            icon_logo = new SpriteSheet(ImageLoader.loadImage("res/icon logo.png"));
            images.put("icon", icon_logo.getBufferedImage());
        } catch (IOException e) {
            Logger.log(Logger.ERR, "image not loaded");
            e.printStackTrace();
        }

        SpriteSheet icon_logo2;
        try {
            icon_logo2 = new SpriteSheet(ImageLoader.loadImage("res/blue.png"));
            images.put("blue", icon_logo2.getBufferedImage());
        } catch (IOException e) {
            Logger.log(Logger.ERR, "image not loaded");
            e.printStackTrace();
        }
        initializeUtility();
    }

    public static void initializeUtility() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageg2d = image.createGraphics();
        imageg2d.setColor(Color.MAGENTA);
        imageg2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        imageg2d.setColor(Color.BLACK);
        imageg2d.setStroke(new BasicStroke(5));
        imageg2d.drawLine(0, 0, image.getWidth(), image.getHeight());
        imageg2d.drawLine(0, image.getWidth(), image.getHeight(), 0);
        imageg2d.dispose();
        images.put("N/A", image);
    }

}
