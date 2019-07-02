package red.mohist.common.asm.remap.transformer;

import red.mohist.common.asm.remap.model.RemapContext;

import java.util.function.Consumer;

/**
 *
 * @author pyz
 * @date 2019/7/1 11:13 PM
 */
public interface ITypeRemapper {
    public abstract int remapType(String type, Consumer<String> setter, RemapContext context);
}
