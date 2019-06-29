package com.maxqia.ReflectionRemapper;

import red.mohist.Mohist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.maxqia.ReflectionRemapper.Utils.mapMethod;
import static com.maxqia.ReflectionRemapper.Utils.reverseMap;
import static com.maxqia.ReflectionRemapper.Utils.reverseMapExternal;

public class RemappedMethods {

    // Classes
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, RemapUtils.getCallerClassloader());
    } // Class.forName(String) grabs the caller's classloader, we replicate that

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (!className.startsWith("net.minecraft.server." + Mohist.getNativeVersion())) {
            return Class.forName(className, initialize, classLoader);
        }
        className = Transformer.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
        return Class.forName(className, initialize, classLoader);
    }

    // Get Fields
    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        return inst.getField(Transformer.remapper.mapFieldName(
                reverseMap(inst), name, null));
    }

    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        return inst.getDeclaredField(Transformer.remapper.mapFieldName(
                reverseMap(inst), name, null));
    }

    // Get Methods
    public static Method getMethod(Class<?> inst, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return inst.getMethod(mapMethod(inst, name, parameterTypes), parameterTypes);
    }

    public static Method getDeclaredMethod(Class<?> inst, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return inst.getDeclaredMethod(mapMethod(inst, name, parameterTypes), parameterTypes);
    }

    // getName
    public static String getName(Field field) {
        String name = field.getName();
        String match = reverseMap(field.getDeclaringClass());

        for (Map.Entry<String, String> entry : Transformer.jarMapping.fields.entrySet()) {
            if (entry.getKey().startsWith(match) && entry.getValue().equals(name)) {
                String[] matched = entry.getKey().split("\\/");
                String rtr =  matched[matched.length-1];
                //System.out.println(matched[matched.length-1] + " field matched " + name);
                return rtr;
            }
        }

        //System.out.println("Could not get field reverse of : " + name);
        return name;
    }

    public static String getName(Method method) {
        String name = method.getName();
        String match = reverseMap(method.getDeclaringClass());

        for (Map.Entry<String, String> entry : Transformer.jarMapping.methods.entrySet()) {
            if (entry.getKey().startsWith(match) && entry.getValue().equals(name)) {
                String[] matched = entry.getKey().split("\\s+")[0].split("\\/");
                String rtr =  matched[matched.length-1];
                //System.out.println(matched[matched.length-1] + " method matched " + name);
                return rtr;
            }
        }

        //System.out.println("Could not get method reverse of : " + name);
        return name;
    }

    // Package names
    public static String getName(Package inst) {
        if (inst == null)
        {
            return null;
        }
        String name = inst.getName();
        //return Transformer.remapper.map(Type.getInternalName(inst)).replace('/', '.');
        String check = name.replace('.', '/').concat("/");
        for (Map.Entry<String, String> entry : Transformer.jarMapping.packages.entrySet()) {
            if (entry.getValue().equals(check)) {
                String match = entry.getKey().replace('/', '.');
                match = match.substring(0, match.length()-1);
                return match;
            }

        }
        //System.out.println(name);
        return name;
    }

    // getSimpleName
    public static String getSimpleName(Class<?> inst) {
        String[] name = getName(inst).split("\\.");
        return name[name.length - 1];
    }

    public static String getName(Class<?> inst) {
        return reverseMapExternal(inst);
    }
}
