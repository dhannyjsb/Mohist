package red.mohist.common.asm.remap.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import red.mohist.MohistConfig;
import red.mohist.common.asm.remap.IRemapTransformer;
import red.mohist.common.asm.remap.model.MethodDesc;
import red.mohist.common.asm.remap.model.RemapContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ListIterator;
import java.util.function.Consumer;

/**
 * 负责nms和cb的多版本转换
 * @author pyz
 * @date 2019/6/30 7:08 AM
 */
public abstract class BaseClassNameTransformer extends IRemapTransformer {

    private final ITypeRemapper remapper;

    public BaseClassNameTransformer(ITypeRemapper remapper) {
        this.remapper = remapper;
    }

    /**
     * 每次执行remap操作后,都要将context中的计数器+1
     * @param context
     * @return
     */
    @Override
    protected byte[] doRemap(RemapContext context) {
        ClassNode classNode = context.getClassNode();
        int modify = 0;
        modify += remap(classNode, context);
        if (modify == 0) {
            return null;
        }
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        byte[] bs = writer.toByteArray();
        try {
            dump(classNode.name, bs);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to dump class:" + classNode.name);
        }
        return bs;
    }

    private void dump(String className, byte[] bs) throws IOException {
        if (!MohistConfig.dumpNMSRemppedClass) {
            return;
        }
        File file = new File(System.getProperty("user.dir") + File.separator + "dumpNMSRemap" + File.separator + className + ".class");
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        Files.write(file.toPath(), bs, StandardOpenOption.TRUNCATE_EXISTING);
    }

    protected int remap(ClassNode classNode, RemapContext context) {
        int n = 0;
        n += remap(classNode.superName, str -> classNode.superName = str, context);
        if (classNode.interfaces != null && classNode.interfaces.size() > 0) {
            for (int i = 0; i < classNode.interfaces.size(); i++) {
                final int index = i;
                n += remap(classNode.interfaces.get(i), str -> classNode.interfaces.set(index, str), context);
            }
        }
        if (classNode.fields != null) {
            for (FieldNode field : classNode.fields) {
                n += remap(field, context);
            }
        }
        if (classNode.methods != null) {
            for (MethodNode method : classNode.methods) {
                n += remap(method, context);
            }
        }
        return n;
    }

    protected int remap(FieldNode node, RemapContext context) {
        int n = 0;
        n += remap(node.desc, str -> node.desc = str, context);
        return n;
    }

    protected int remap(MethodNode node, RemapContext context) {
        int n = 0;
        n += remapMethodDesc(node.desc, str -> node.desc = str, context);
        if (node.instructions != null) {
            ListIterator<AbstractInsnNode> it = node.instructions.iterator();
            while (it.hasNext()) {
                n += remap(node, it.next(), context);
            }
        }
        if (node.localVariables != null) {
            for (LocalVariableNode localVariable : node.localVariables) {
                n += remap(node, localVariable, context);
            }
        }
        return n;
    }

    protected int remap(MethodNode node, AbstractInsnNode insn, RemapContext context) {
        int n = 0;
        if (insn instanceof MethodInsnNode) {
            n += remap(node, (MethodInsnNode) insn, context);
        } else if (insn instanceof InvokeDynamicInsnNode) {
            n += remap(node, (InvokeDynamicInsnNode) insn, context);
        } else if (insn instanceof TypeInsnNode) {
            n += remap(node, (TypeInsnNode) insn, context);
        } else if (insn instanceof FieldInsnNode) {
            n += remap(node, (FieldInsnNode) insn, context);
        }
        return n;
    }

    protected int remap(MethodNode node, MethodInsnNode insn, RemapContext context) {
        int n = 0;
        n += remapMethodDesc(insn.desc, str -> insn.desc = str, context);
        n += remap(insn.owner, str -> insn.owner = str, context);
        return n;
    }

    protected int remap(MethodNode node, TypeInsnNode insn, RemapContext context) {
        int n = 0;
        n += remap(insn.desc, str -> insn.desc = str, context);
        return n;
    }

    protected int remap(MethodNode node, LocalVariableNode localVariable, RemapContext context) {
        int n = 0;
        n += remap(localVariable.desc, str -> localVariable.desc = str, context);
        n += remap(localVariable.signature, str -> localVariable.signature = str, context);
        return n;
    }

    protected int remap(MethodNode node, FieldInsnNode insn, RemapContext context) {
        int n = 0;
        n += remap(insn.desc, str -> insn.desc = str, context);
        n += remap(insn.owner, str -> insn.owner = str, context);
        return n;
    }

    protected int remap(MethodNode node, InvokeDynamicInsnNode insn, RemapContext context) {
        int n = 0;
        n += remapMethodDesc(insn.desc, str -> insn.desc = str, context);
        if (insn.bsm != null) {
            n += remapHandle(node, insn, insn.bsm, handle -> insn.bsm = handle, context);
        }
        if (insn.bsmArgs != null) {
            for (int i = 0; i < insn.bsmArgs.length; i++) {
                int index = i;
                Object bsmArg = insn.bsmArgs[i];
                if (bsmArg instanceof Type) {
                    n += remap(node, insn, (Type) bsmArg, t -> insn.bsmArgs[index] = t, context);
                } else if (bsmArg instanceof Handle) {
                    n += remapHandle(node, insn, (Handle) bsmArg, handle -> insn.bsmArgs[index] = handle, context);
                }
            }
        }
        return n;
    }

    protected int remap(MethodNode node, InvokeDynamicInsnNode insn, Type methodType, Consumer<Type> setter, RemapContext context) {
        int n = 0;
        n += remap(MethodDesc.from(methodType), str -> setter.accept(str.toType()), context);
        return n;
    }

    protected int remap(MethodDesc methodType, Consumer<MethodDesc> setter, RemapContext context) {
        int n = 0;
        n += remap(methodType.getReturnType().getDescriptor(), str -> methodType.setReturnType(Type.getType(str)), context);
        for (int i = 0; i < methodType.getParameterTypes().length; i++) {
            final int index = i;
            n += remap(methodType.getParameterTypes()[i].getDescriptor(), str -> methodType.getParameterTypes()[index] = Type.getType(str), context);
        }
        if (n > 0) {
            setter.accept(methodType);
        } else {
            setter.accept(methodType);
        }
        return n;
    }

    protected int remapType(Type type, Consumer<Type> setter, RemapContext context) {
        int n = 0;
        Type[] rt = new Type[]{type.getReturnType(), null};
        Type[] rats = type.getArgumentTypes();
        n += remap(rt[0].getDescriptor(), str -> {
            rt[1] = Type.getType(str);
        }, context);
        for (int i = 0; i < rats.length; i++) {
            int index = i;
            n += remap(rats[i].getDescriptor(), str -> {
                rats[index] = Type.getType(str);
            }, context);
        }
        Type rType = Type.getMethodType(rt[1], rats);
        setter.accept(rType);
        return n;
    }

    protected int remapHandle(MethodNode node, InvokeDynamicInsnNode insn, Handle handle, Consumer<Handle> setter, RemapContext context) {
        int n = 0;
        String[] rs = new String[2];
//        n += remap(handle.getOwner(), str -> rs[0] = str, context);
        n += remapMethodDesc(handle.getDesc(), str -> rs[1] = str, context);
        if (n == 0) {
            setter.accept(handle);
        } else {
            setter.accept(new Handle(handle.getTag(), handle.getOwner(), handle.getName(), rs[1], handle.isInterface()));
        }
        return n;
    }

    protected int remapMethodDesc(String desc, Consumer<String> setter, RemapContext context) {
        int n = 0;
        Type r = Type.getReturnType(desc);
        Type[] args = Type.getArgumentTypes(desc);
        Type[] rr = new Type[1];
        n += remap(r.getDescriptor(), str -> rr[0] = Type.getType(str), context);
        for (int i = 0; i < args.length; i++) {
            final int index = i;
            n += remap(args[i].getDescriptor(), str -> args[index] = Type.getType(str), context);
        }
        if (n > 0) {
            setter.accept(Type.getMethodDescriptor(rr[0], args));
        } else {
            setter.accept(desc);
        }
        return n;
    }

    protected int remap(String old, Consumer<String> setter, RemapContext context) {
        int n = 0;
        if (old == null || old.isEmpty()) {
            return 0;
        }
        n += remapper.remapType(old, setter, context);
        return n;
    }


}
