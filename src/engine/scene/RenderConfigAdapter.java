package engine.scene;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import engine.registry.render.RenderConfig;
import engine.registry.render.buff.image.ImageRendererConfig;
import engine.registry.render.rect.RectRenderConfig;

// Assuming you have a type field in your JSON, like: { "type": "image", ... }
public class RenderConfigAdapter implements JsonDeserializer<RenderConfig>, JsonSerializer<RenderConfig> {

    @Override
    public JsonElement serialize(RenderConfig src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();

        if (src instanceof ImageRendererConfig) {
            jsonObject.addProperty("type", "image");
        }
        if (src instanceof RectRenderConfig) {
            jsonObject.addProperty("type", "rect");
        }

        // Add more instanceof cases if needed for other subtypes

        return jsonObject;
    }

    @Override
    public RenderConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "image":
                return context.deserialize(jsonObject, ImageRendererConfig.class);
            case "rect":
                return context.deserialize(jsonObject, RectRenderConfig.class);

            // Add other cases here for different subtypes
            // case "pdf":
            // return context.deserialize(jsonObject, PdfRendererConfig.class);

            default:
                throw new JsonParseException("Unknown RenderConfig type: " + type);
        }
    }
}
