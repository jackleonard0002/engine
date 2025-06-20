package engine.registry.comp.script;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import engine.scene.panel.PanelCoreScript;

public class ScriptableCompAdapter implements JsonSerializer<ScriptableComp>, JsonDeserializer<ScriptableComp> {

    @Override
    public JsonElement serialize(ScriptableComp src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        // Serialize base component data, if any
        json.addProperty("type", "engine.registry.comp.script.ScriptableComp"); // Assuming ScriptableComponent has a
                                                                                // name or id

        JsonArray scriptsArray = new JsonArray();
        for (Script script : src.getScripts()) {
            JsonObject scriptJson = new JsonObject();
            scriptJson.addProperty("type", script.getClass().getSimpleName()); // Or a custom type key
            scriptJson.add("data", context.serialize(script));
            scriptsArray.add(scriptJson);
        }

        json.add("data", scriptsArray);
        return json;
    }

    @Override
    public ScriptableComp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        ScriptableComp component = new ScriptableComp(); // Or a factory if it's abstract

        JsonArray scriptsArray = jsonObject.getAsJsonArray("scripts");
        for (JsonElement elem : scriptsArray) {
            JsonObject scriptObj = elem.getAsJsonObject();
            String type = scriptObj.get("type").getAsString();
            JsonElement data = scriptObj.get("data");

            // Simple type dispatch—replace with a registry if needed
            Script script = switch (type) {
                case "engine.scene.panel.PanelCoreScript" -> context.deserialize(data, PanelCoreScript.class);
                default -> throw new JsonParseException("Unknown script type: " + type);
            };

            component.addScript(script);
        }

        return component;
    }
}
