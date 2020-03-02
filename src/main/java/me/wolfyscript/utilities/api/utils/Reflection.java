package me.wolfyscript.utilities.api.utils;

import me.wolfyscript.utilities.main.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Reflection {

    /*
     * The server version string to location NMS & OBC classes
     */
    private static String versionString;

    /*
     * Cache of NMS classes that we've searched for
     */
    private static Map<String, Class<?>> loadedNMSClasses = new HashMap<>();

    /*
     * Cache of OBS classes that we've searched for
     */
    private static Map<String, Class<?>> loadedOBCClasses = new HashMap<>();

    /*
     * Cache of methods that we've found in particular classes
     */
    private static Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<>();

    /*
     * Cache of fields that we've found in particular classes
     */
    private static Map<Class<?>, Map<String, Field>> loadedFields = new HashMap<>();
    private static Map<Class<?>, Map<Class, Field>> foundFields = new HashMap<>();

    public static String getVersion() {
        return Main.getInstance().getServer().getClass().getPackage().getName().substring(23);
    }

    /**
     * Get a class from the org.bukkit.craftbukkit package
     *
     * @param obcClassName the path to the class
     * @return the found class at the specified path
     */
    public synchronized static Class<?> getOBC(String obcClassName) {
        if (loadedOBCClasses.containsKey(obcClassName)) {
            return loadedOBCClasses.get(obcClassName);
        }

        String clazzName = "org.bukkit.craftbukkit." + getVersion() + "." + obcClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            loadedOBCClasses.put(obcClassName, null);
            return null;
        }

        loadedOBCClasses.put(obcClassName, clazz);
        return clazz;
    }

    /**
     * Get an NMS Class
     *
     * @param nmsClassName The name of the class
     * @return The class
     */
    public static Class<?> getNMS(String nmsClassName) {
        if (loadedNMSClasses.containsKey(nmsClassName)) {
            return loadedNMSClasses.get(nmsClassName);
        }

        String clazzName = "net.minecraft.server." + getVersion() + "." + nmsClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return loadedNMSClasses.put(nmsClassName, null);
        }

        loadedNMSClasses.put(nmsClassName, clazz);
        return clazz;
    }

    /**
     * Get a Bukkit {@link Player} players NMS playerConnection object
     *
     * @param player The player
     * @return The players connection
     */
    public static Object getConnection(Player player) {
        Method getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod != null) {
            try {
                Object nmsPlayer = getHandleMethod.invoke(player);
                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");
                return playerConField.get(nmsPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get a classes constructor
     *
     * @param clazz  The constructor class
     * @param params The parameters in the constructor
     * @return The constructor object
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Get a method from a class that has the specific paramaters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @param params     Any parameters that the method has
     * @return The method with appropriate paramaters
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<String, Method>());
        }

        Map<String, Method> methods = loadedMethods.get(clazz);

        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            loadedMethods.put(clazz, methods);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            loadedMethods.put(clazz, methods);
            return null;
        }
    }

    /**
     * Get a field with a particular name from a class
     *
     * @param clazz     The class
     * @param fieldName The name of the field
     * @return The field object
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        if (!loadedFields.containsKey(clazz)) {
            loadedFields.put(clazz, new HashMap<String, Field>());
        }

        Map<String, Field> fields = loadedFields.get(clazz);

        if (fields.containsKey(fieldName)) {
            return fields.get(fieldName);
        }

        try {
            Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            loadedFields.put(clazz, fields);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            loadedFields.put(clazz, fields);
            return null;
        }
    }

    /**
     * Gets the first Field with the correct return type
     *
     * @param clazz     The class
     * @param type The return type
     * @return The field object
     */
    public static Field findField(Class<?> clazz, Class type) {
        if (!foundFields.containsKey(clazz)) {
            foundFields.put(clazz, new HashMap<>());
        }
        Map<Class, Field> fields = foundFields.get(clazz);
        if (fields.containsKey(type)) {
            return fields.get(type);
        }
        try {
            List<Field> allFields = new ArrayList<>();
            allFields.addAll(Arrays.asList(clazz.getFields()));
            allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            for(Field f : allFields){
                if(type.equals(f.getType())){
                    fields.put(type, f);
                    foundFields.put(clazz, fields);
                    return f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fields.put(type, null);
            foundFields.put(clazz, fields);
        }
        return null;
    }
}
