package red.mohist.common.asm.remap.proxy.asm;

import org.objectweb.asm.ClassWriter;
import red.mohist.common.asm.remap.RemapUtils;

import java.io.IOException;

/**
 *
 * @author pyz
 * @date 2019/7/15 8:52 PM
 */
public class ProxyClassWriter {

    public static byte[] toByteArray(ClassWriter cw) {
        byte[] code = cw.toByteArray();
        try {
            return RemapUtils.remapFindClass(null, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
