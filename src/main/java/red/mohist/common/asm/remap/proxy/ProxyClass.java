package red.mohist.common.asm.remap.proxy;


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
        return forName(RemapUtils.remapClassNameV2(className.replace('.', '/')), true, RemapUtils.getCallerClassLoder());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(RemapUtils.remapClassNameV2(className.replace('.', '/')), initialize, loader);
    }

    public Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(RemapUtils.remapMethodNameV2(clazz.getName().replace('.', '/'), name, "(Ljava/lang/String;)Ljava/lang/reflect/Method;"), parameterTypes);
    }

    public Method getMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getMethod(RemapUtils.remapMethodNameV2(clazz.getName().replace('.', '/'), name, "(Ljava/lang/String;)Ljava/lang/reflect/Method;"), parameterTypes);
    }

    public Field getDeclaredField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(RemapUtils.remapFieldName(clazz.getName().replace('.', '/'), name));
    }

    public Field getField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getField(RemapUtils.remapFieldName(clazz.getName().replace('.', '/'), name));
    }
}
