package red.mohist.common.asm.remap.proxy;


import red.mohist.common.asm.remap.ASMUtils;
import red.mohist.common.asm.remap.RemapUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author pyz
 * @date 2019/7/1 12:24 AM
 */
public class ProxyClass {

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, RemapUtils.getCallerClassLoder());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(ASMUtils.toClassName(RemapUtils.map(className.replace('.', '/'))), initialize, loader);
    }

    public static Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(RemapUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Method getMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getMethod(RemapUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Field getDeclaredField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(RemapUtils.mapFieldName(clazz, name));
    }

    public static Field getField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getField(RemapUtils.mapFieldName(clazz, name));
    }
}
