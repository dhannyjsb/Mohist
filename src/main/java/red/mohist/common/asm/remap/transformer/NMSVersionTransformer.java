package red.mohist.common.asm.remap.transformer;

import red.mohist.Mohist;
import red.mohist.common.asm.remap.model.RemapContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 负责nms和cb的多版本转换
 * @author pyz
 * @date 2019/6/30 7:08 AM
 */
public class NMSVersionTransformer extends BaseClassNameTransformer {

    private static final String targetVersion = Mohist.getNativeVersion();
    private static final Pattern cbPattern = Pattern.compile("org/bukkit/craftbukkit/(v\\d_\\d+_R\\d)/[\\w/]+");
    private static final Pattern nmsPattern = Pattern.compile("net/minecraft/server/(v\\d_\\d+_R\\d)/[\\w/]+");
    private static final ITypeRemapper remapper = new ITypeRemapper() {
        @Override
        public int remapType(String type, Consumer<String> setter, RemapContext context) {
            int n = 0;
            String str = type;
            if (str == null || str.isEmpty()) {
                return n;
            }
            Matcher m = cbPattern.matcher(type);
            if (m.find()) {
                String srcVersion = m.group(1);
                if (!Objects.equals(srcVersion, targetVersion)) {
                    str = str.replace(m.group(1), targetVersion);
                    context.remapCount++;
                    n++;
                }
            } else {
                m = nmsPattern.matcher(type);
                if (m.find()) {
                    String srcVersion = m.group(1);
                    if (!Objects.equals(srcVersion, targetVersion)) {
                        str = str.replace(m.group(1), targetVersion);
                        context.remapCount++;
                        n++;
                    }
                }
            }
            setter.accept(str);
            return n;
        }
    };

    public NMSVersionTransformer() {
        super(remapper);
    }

    @Override
    protected boolean canRemap(RemapContext context) {
        return !context.getClassNode().name.startsWith("net/minecraft/server/") &&
                !context.getClassNode().name.startsWith("org/bukkit/") &&
                !context.getClassNode().name.startsWith("red/mohist/")
                ;
    }

    /**
     * 升序
     * @return
     */
    @Override
    protected int order() {
        return -1000;
    }
}
