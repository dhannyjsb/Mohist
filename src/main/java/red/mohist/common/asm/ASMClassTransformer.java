package red.mohist.common.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ASMClassTransformer implements IClassTransformer
{
    public ASMClassTransformer() {
        super();
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return basicClass;
        }
        if ("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1".equals(transformedName)) {
            basicClass = this.transformClass(basicClass);
        }
        return basicClass;
    }

    private byte[] transformClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(1);
        classNode.access = 33;
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
