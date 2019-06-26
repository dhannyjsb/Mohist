package red.mohist.common.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import red.mohist.common.asm.IAutoRegisterClassTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 *
 * @author pyz
 * @date 2019/6/26 10:35 AM
 */
public class StaticMethodRedirectTransformer implements IAutoRegisterClassTransformer {
    private final List<StaticMethodRedirect> redirects = new ArrayList<>();

    public StaticMethodRedirectTransformer() {
        register();
    }

    private void register() {
//        将所有Math.sin和Math.cos调用转换成MathHelper.sin和MathHelper.cos调用
        redirects.add(new StaticMethodRedirect("Ljava/lang/Math;", "sin", "(D)D", "Lnet/minecraft/util/math/MathHelper;"));
        redirects.add(new StaticMethodRedirect("Ljava/lang/Math;", "cos", "(D)D", "Lnet/minecraft/util/math/MathHelper;"));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        for (MethodNode m : classNode.methods) {
            redirectMethod(m);
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void redirectMethod(MethodNode methodNode) {
        for (ListIterator<AbstractInsnNode> it = methodNode.instructions.iterator(); it.hasNext(); ) {
            AbstractInsnNode insnNode = it.next();
            if (insnNode.getType() != AbstractInsnNode.METHOD_INSN) {
                continue;
            }
            MethodInsnNode node = (MethodInsnNode) insnNode;
            if (node.getOpcode() == Opcodes.INVOKESTATIC) {
                redirectMethod(node);
            }
        }
    }

    private void redirectMethod(MethodInsnNode node) {
        for (StaticMethodRedirect redirect : redirects) {
            if (redirect.fromType.equals(node.owner) && redirect.name.equals(node.name) && redirect.desc.equals(node.desc)) {
                node.owner = redirect.toType;
                return;
            }
        }
    }

    public static class StaticMethodRedirect {
        private final String fromType;
        private final String desc;
        private final String name;
        private final String toType;

        public StaticMethodRedirect(String fromType, String name, String desc, String toType) {
            Objects.requireNonNull(fromType);
            Objects.requireNonNull(desc);
            Objects.requireNonNull(name);
            Objects.requireNonNull(toType);
            this.fromType = fromType;
            this.desc = desc;
            this.name = name;
            this.toType = toType;
        }
    }
}
