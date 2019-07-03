package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.RemapperProcessor;
import red.mohist.common.asm.remap.ASMUtils;

/**
 * 负责nsm->mcp的remap
 *
 * @author pyz
 * @date 2019/7/3 10:38 PM
 */
public class MohistJarRemapper extends JarRemapper {

    public MohistJarRemapper(RemapperProcessor preProcessor, JarMapping jarMapping, RemapperProcessor postProcessor) {
        super(preProcessor, jarMapping, postProcessor);
    }

    public MohistJarRemapper(RemapperProcessor remapperPreprocessor, JarMapping jarMapping) {
        super(remapperPreprocessor, jarMapping);
    }

    public MohistJarRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        if (ASMUtils.isValidSingnature(signature)) {
            return super.mapSignature(signature, typeSignature);
        } else {
            return signature;
        }
    }
}
