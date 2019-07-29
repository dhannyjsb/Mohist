package red.mohist.common.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.function.Predicate;

public class ASMClassTransformer implements IClassTransformer
{
    private final Predicate<? super MethodNode> CLIENTSIDE = (method) ->
            method.desc.contains("Lnet/minecraft/client/") && (
                    method.desc.contains("Lnet/minecraft/client/util/ITooltipFlag") ||
                            (method.desc.contains("Lnet/minecraft/client/renderer/") && !method.desc.contains("Lnet/minecraft/client/renderer/block/model/ModelResourceLocation"))
            );

    public ASMClassTransformer() {
        super();
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return basicClass;
        }
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        if (classNode.methods.removeIf(CLIENTSIDE)) {
            ClassWriter writer = new ClassWriter(0);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        if ("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1".equals(transformedName)) {
            basicClass = this.transformClass(basicClass);
        }
        return basicClass;
    }

    private byte[] transformClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sendPacket", "(Lnet/minecraft/network/Packet;)V", "(Lnet/minecraft/network/Packet<*>;)V", null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/network/NetHandlerPlayServer", "func_147359_a", "(Lnet/minecraft/network/Packet;)V", true);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();

        classNode.access = Opcodes.ACC_SUPER + Opcodes.ACC_PUBLIC;

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
