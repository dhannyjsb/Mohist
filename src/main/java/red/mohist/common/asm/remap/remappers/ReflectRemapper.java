package red.mohist.common.asm.remap.remappers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:05 PM
 */
public class ReflectRemapper extends Remapper implements ClassRemapperSupplier {

    @Override
    public ClassRemapper getClassRemapper(ClassWriter classWriter) {
        return new MohistClassRemapper(classWriter, this);
    }
}
