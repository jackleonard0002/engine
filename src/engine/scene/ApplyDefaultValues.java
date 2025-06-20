package engine.scene;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import engine.registry.Component;
import engine.registry.EntityMaterial;
import engine.registry.comp.BoundsComp;
import engine.registry.comp.TransformComp;
import engine.util.Logger;

public class ApplyDefaultValues {

    private static Logger logger = new Logger(ApplyDefaultValues.class, false);

    public static void AutoInit(Scene scene) {
        for (EntityMaterial em : scene.getRegistry().getEntityMap().values()) {
            for (Component comp : em.getComponents()) {
                if (comp instanceof BoundsComp boundsComp) {
                    instantiateEmptyFields(boundsComp.getBounds());
                }
                if (comp instanceof TransformComp transformComp) {
                    instantiateEmptyFields(transformComp.getTransform());
                }
                instantiateEmptyFields(comp);
            }
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T> void instantiateEmptyFields(T instance) {
        Class<?> clazz = instance.getClass();
        List<Field> fields = getAllFields(clazz);

        logger.iLog(Logger.WARM, "Class: " + clazz.getName());

        for (Field field : fields) {
            logger.iLog(Logger.WARM, "       Field: " + field.getName());
            field.setAccessible(true);

            // Only proceed if @AutoInstantiate is present and true
            AutoInstantiate marker = field.getAnnotation(AutoInstantiate.class);
            if (marker == null || !marker.value())
                continue;

            try {
                Object currentValue = field.get(instance);
                Class<?> type = field.getType();

                if (!type.isPrimitive() && currentValue == null) {
                    Object newValue = tryInstantiate(type);
                    logger.iLog(Logger.EXOT, "       Set Field: " + field.getName());
                    field.set(instance, newValue);
                    if (field.getName().equals("bounds")) {
                        logger.iLog(Logger.EXOT, "       Set Field To: " + field.get(instance).toString());
                    }
                }
            } catch (Exception e) {
                logger.iLog(Logger.ERR, "Error setting field: " + field.getName());
                throw new RuntimeException("Error setting field: " + field.getName(), e);
            }
        }
    }

    private static Object tryInstantiate(Class<?> type) {
        try {
            if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
                return instantiateFallback(type); // e.g., default implementations for interfaces
            }

            Constructor<?> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception e) {
            return null; // Fails silently or log as needed
        }
    }

    private static Object instantiateFallback(Class<?> iface) {
        if (iface == List.class)
            return new ArrayList<>();
        if (iface == Set.class)
            return new HashSet<>();
        if (iface == Map.class)
            return new HashMap<>();
        if (iface == Queue.class)
            return new LinkedList<>();
        return null;
    }

    // Basic String parsing (extend as needed)
    protected static Object parseValue(Class<?> type, String value) {
        if (type == String.class)
            return value;
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(value);
        if (type == long.class || type == Long.class)
            return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(value);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(value);
        if (type == float.class || type == Float.class)
            return Float.parseFloat(value);
        if (type == char.class || type == Character.class)
            return value.charAt(0);
        throw new UnsupportedOperationException("Type not supported: " + type);
    }

    public static void deepAutoInstantiate(Object instance) {
        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        deepAutoInstantiateInternal(instance, visited);
    }

    private static void deepAutoInstantiateInternal(Object instance, Set<Object> visited) {
        if (instance == null || visited.contains(instance) || isJdkType(instance.getClass()))
            return;

        visited.add(instance);
        Class<?> clazz = instance.getClass();
        logger.iLog(Logger.WARM, "Class: " + clazz.getName());

        if (instance instanceof Collection<?> collection) {
            for (Object item : collection) {
                deepAutoInstantiateInternal(item, visited);
            }
            return;
        }

        if (instance.getClass().isArray()) {
            int length = Array.getLength(instance);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(instance, i);
                deepAutoInstantiateInternal(element, visited);
            }
            return;
        }

        if (instance instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                deepAutoInstantiateInternal(value, visited);
            }
            return;
        }

        while (clazz != null) {
            for (Field field : getAllFields(clazz)) {
                field.setAccessible(true);
                logger.iLog(Logger.WARM, "       Field: " + field.getName());
                try {
                    Object currentValue = field.get(instance);
                    Class<?> fieldType = field.getType();

                    AutoInstantiate marker = field.getAnnotation(AutoInstantiate.class);
                    if (currentValue == null && !fieldType.isPrimitive() && marker != null && marker.value()) {
                        Object newValue = tryInstantiate(fieldType);
                        if (newValue != null) {
                            field.set(instance, newValue);
                            currentValue = newValue;
                            logger.iLog(Logger.EXOT, "       Set Field: " + field.getName());
                            if (field.getName().equals("bounds")) {
                                logger.iLog(Logger.EXOT, "       Set Field To: " + field.get(instance));
                            }
                        }
                    }

                    if (currentValue != null) {
                        deepAutoInstantiateInternal(currentValue, visited);
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Error processing field: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static boolean isJdkType(Class<?> type) {
        return type.getName().startsWith("java.") || type.isEnum() || type.isPrimitive();
    }

}
