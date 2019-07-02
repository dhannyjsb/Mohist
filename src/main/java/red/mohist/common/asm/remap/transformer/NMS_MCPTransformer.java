package red.mohist.common.asm.remap.transformer;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import red.mohist.common.asm.remap.RemapUtils;
import red.mohist.common.asm.remap.model.RemapContext;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 负责nms到mcp的转换
 *
 * @author pyz
 * @date 2019/7/1 10:47 PM
 */
public class NMS_MCPTransformer extends BaseClassNameTransformer {
    private static final ITypeRemapper remapper = new ITypeRemapper() {
        @Override
        public int remapType(String type, Consumer<String> setter, RemapContext context) {
            int n = 0;
            int start = 0;
            String prefix = "";
            String suffix = "";
            while (true) {
                char c = type.charAt(start);
                if (c == 'L' || c == '[') {
                    start++;
                    prefix += c;
                } else {
                    break;
                }
            }
            int end = type.endsWith(";") ? type.length() - 1 : type.length();
            if (end > 0) {
                suffix = ";";
            }
            type = type.substring(start, end);
            String str = RemapUtils.remapClassNameV2(type);
            if (!Objects.equals(str, type)) {
                context.remapCount++;
                n++;
            }
            setter.accept(prefix + str + suffix);
            return n;
        }
    };

    public NMS_MCPTransformer() {
        super(remapper);
    }

    /**
     * 每次执行remap操作后,都要将context中的计数器+1
     * @param context
     * @return
     */
    @Override
    protected byte[] doRemap(RemapContext context) {
        return super.doRemap(context);
    }

    @Override
    protected int remap(FieldNode node, RemapContext context) {
        int n = 0;
        String remapFieldName = RemapUtils.remapFieldNameV2(context.getClassNode().name, node.name);
        if (!Objects.equals(node.name, remapFieldName)) {
            node.name = remapFieldName;
            n++;
            context.remapCount++;
        }
        n += super.remap(node, context);
        return n;
    }

    @Override
    protected int remap(MethodNode node, RemapContext context) {
        int n = 0;
        String remapMethodName = RemapUtils.remapMethodNameV2(context.getClassNode().name, node.name, node.desc);
        if (!Objects.equals(node.name, remapMethodName)) {
            node.name = remapMethodName;
            n++;
            context.remapCount++;
        }
        n += super.remap(node, context);
        return n;
    }

    @Override
    protected boolean canRemap(RemapContext context) {
        return !context.getClassNode().name.startsWith("net/minecraft/server/") &&
                !context.getClassNode().name.startsWith("org/bukkit/") &&
                !context.getClassNode().name.startsWith("red/mohist/")
                ;
    }
}
