package engine.registry.comp.script;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptAdapter extends TypeAdapter<Script> {
    private final Gson gson;

    public ScriptAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Script value) throws IOException {
        out.beginObject();
        out.name("type").value("" + value.getClass().getName());
        out.name("data");
        gson.toJson(value, value.getClass(), out);
        out.endObject();
    }

    @Override
    public Script read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (Script) gson.fromJson(data, clazz);
    }

}
