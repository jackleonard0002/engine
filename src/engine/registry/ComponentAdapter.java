package engine.registry;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import engine.registry.comp.script.ScriptableComp;
import engine.registry.comp.script.ScriptableCompAdapter;

public class ComponentAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

    @Override
    public JsonElement serialize(Component component, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();

        // Store the full class name
        wrapper.addProperty("class", component.getClass().getName());

        // Serialize the component normally
        JsonElement data = context.serialize(component);
        wrapper.add("data", data);

        return wrapper;
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject wrapper = json.getAsJsonObject();

        String className = wrapper.get("class").getAsString();
        JsonElement data = wrapper.get("data");

        try {
            Class<?> clazz = Class.forName(className);

            if (!Component.class.isAssignableFrom(clazz)) {
                throw new JsonParseException("Class " + className + " does not implement Component");
            }

            // Handle ScriptableComponent specially
            if (ScriptableComp.class.isAssignableFrom(clazz)) {
                return new ScriptableCompAdapter().deserialize(data, clazz, context);
            }

            return context.deserialize(data, clazz);

        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown component class: " + className, e);
        }
    }
}
