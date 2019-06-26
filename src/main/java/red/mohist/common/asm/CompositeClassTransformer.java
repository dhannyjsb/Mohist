package red.mohist.common.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 聚合多个转换器
 *
 * @author pyz
 * @date 2019/6/26 10:40 AM
 */
public class CompositeClassTransformer implements IClassTransformer {
    private final List<IAutoRegisterClassTransformer> transformers = new ArrayList<>();

    public CompositeClassTransformer() {
        for (IAutoRegisterClassTransformer iAutoRegisterClassTransformer : ServiceLoader.load(IAutoRegisterClassTransformer.class)) {
            transformers.add(iAutoRegisterClassTransformer);
        }
//        按order升序
        transformers.sort(Comparator.comparingInt(IAutoRegisterClassTransformer::order));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (IClassTransformer transformer : transformers) {
            basicClass = transformer.transform(name, transformedName, basicClass);
        }
        return basicClass;
    }
}
