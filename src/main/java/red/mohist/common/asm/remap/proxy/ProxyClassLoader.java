package red.mohist.common.asm.remap.proxy;

import red.mohist.common.asm.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:09 PM
 */
public class ProxyClassLoader {
    public static final String desc = ProxyClassLoader.class.getName().replace('.', '/');

    public static Class<?> loadClass(final ClassLoader inst, String className) throws ClassNotFoundException {
        className = RemapUtils.remapClassNameV2(className.replace('.', '/'));
        return inst.loadClass(className);
    }

}
