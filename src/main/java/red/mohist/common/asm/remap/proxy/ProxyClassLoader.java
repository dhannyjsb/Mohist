package red.mohist.common.asm.remap.proxy;

import org.objectweb.asm.Type;
import red.mohist.common.asm.remap.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/1 8:09 PM
 */
public class ProxyClassLoader {
    public static final String desc = Type.getDescriptor(ProxyClassLoader.class);

    public static Class<?> loadClass(final ClassLoader inst, String className) throws ClassNotFoundException {
        className = RemapUtils.remapClassNameV1(className);
        return inst.loadClass(className);
    }

}
