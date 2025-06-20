package engine.scene;

import java.awt.Color;
import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ColorTypeAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value("#" + Integer.toHexString(value.getRGB()).substring(2)); // "#RRGGBB"
        }
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        String hex = in.nextString();
        return Color.decode(hex); // parses "#RRGGBB"
    }
}
