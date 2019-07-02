package red.mohist.common.asm.remap.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import red.mohist.common.asm.remap.IRemapTransformer;
import red.mohist.common.asm.remap.MethodRedirectUtils;
import red.mohist.common.asm.remap.RemapUtils;
import red.mohist.common.asm.remap.model.MethodRedirectRule;
import red.mohist.common.asm.remap.model.RemapContext;
import red.mohist.common.asm.remap.proxy.ProxyClassLoader;

import java.util.ListIterator;
import java.util.Map;

/**
 * 负责反射的remap
 *
 * @author pyz
 * @date 2019/7/1 10:47 PM
 */
public class ReflectTransformer extends IRemapTransformer {
    /**
     * 每次执行remap操作后,都要将context中的计数器+1
     * @param context
     * @return
     */
    @Override
    protected byte[] doRemap(RemapContext context) {
        ClassNode classNode = context.getClassNode();
        for (MethodNode method : classNode.methods) {
            ListIterator<AbstractInsnNode> it = method.instructions.iterator();
            while (it.hasNext()) {
                AbstractInsnNode insn = it.next();
                if (insn instanceof TypeInsnNode) {
                    TypeInsnNode typeInsnNode = (TypeInsnNode) insn;
//                     new URLClassLoader
                    if (insn.getOpcode() == Opcodes.NEW && typeInsnNode.desc.equals("java/net/URLClassLoader")) {
                        typeInsnNode.desc = ProxyClassLoader.desc;
                        context.remapCount++;
                    }
                    continue;
                }
                if (!(insn instanceof MethodInsnNode)) {
                    continue;
                }
                MethodInsnNode m = (MethodInsnNode) insn;
                Map<String, Map<String, MethodRedirectRule>> byOwner = MethodRedirectUtils.methodRedirectMapping.get(m.owner);
                if (byOwner == null) {
                    continue;
                }
                Map<String, MethodRedirectRule> byName = byOwner.get(m.name);
                if (byName == null) {
                    continue;
                }
                MethodRedirectRule ir = byName.get(RemapUtils.toMethodArgDesc(m.desc));
                if (ir == null) {
                    continue;
                }
                if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                    context.remapCount += MethodRedirectUtils.redirectVirtual(m, ir.getRemapOwner(), m.name);
                } else if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
                    context.remapCount += MethodRedirectUtils.redirectStatic(m, ir.getRemapOwner(), m.name);
                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    /**
     * 升序
     * @return
     */
    @Override
    protected int order() {
        return 1000;
    }
}
