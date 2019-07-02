package red.mohist.common.asm.remap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:16 PM
 */
public class ASMUtils {

    private static final Map<Integer, String> opcodeMap = new HashMap<>();
    private static final Map<Integer, String> typeMap = new HashMap<>();
    private static final Map<Integer, BiConsumer<String, AbstractInsnNode>> printerMap = new HashMap<>();

    static {
        for (Field field : Opcodes.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    opcodeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field field : AbstractInsnNode.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    typeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        printerMap.put(AbstractInsnNode.FIELD_INSN, (prefix, item) -> {
            FieldInsnNode node = (FieldInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.VAR_INSN, (prefix, item) -> {
            VarInsnNode node = (VarInsnNode) item;
            System.out.println(prefix + " " + node.var);
        });
        printerMap.put(AbstractInsnNode.TYPE_INSN, (prefix, item) -> {
            TypeInsnNode node = (TypeInsnNode) item;
            System.out.println(prefix + " " + node.desc);
        });

        printerMap.put(AbstractInsnNode.METHOD_INSN, (prefix, item) -> {
            MethodInsnNode node = (MethodInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.INVOKE_DYNAMIC_INSN, (prefix, item) -> {
            InvokeDynamicInsnNode node = (InvokeDynamicInsnNode) item;
            System.out.println(prefix + " " + node.name + " " + node.desc);
            if (node.bsm != null) {
                print(prefix, node.bsm);
            }
            if (node.bsmArgs != null) {
                for (Object bsmArg : node.bsmArgs) {
                    if (bsmArg instanceof Type) {
                        print(prefix, (Type) bsmArg);
                    } else if (bsmArg instanceof Handle) {
                        print(prefix, (Handle) bsmArg);
                    } else {
                        throw new RuntimeException("未知类型:" + bsmArg.getClass().getName());
                    }
                }
            }
        });
    }

    public static void dump(String dir, byte[] bs) throws IOException {
        ClassReader classReader = new ClassReader(bs);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        File file = new File(dir, classNode.name + ".class");
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        Files.write(file.toPath(), bs);
    }

    public static String getOpcodeName(int opcode) {
        return opcodeMap.get(opcode);
    }

    public static String getTypeName(int type) {
        return opcodeMap.get(type);
    }

    public static void printClass(byte[] bs) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bs);
        classReader.accept(classNode, 0);
        System.out.println("============ " + classNode.name + " ============");
        for (FieldNode field : classNode.fields) {
            System.out.println("  field " + field.desc + " " + field.name + " " + field.signature);
        }
        for (MethodNode method : classNode.methods) {
            System.out.println("  method " + method.name + " " + method.desc);
            ListIterator<AbstractInsnNode> it = method.instructions.iterator();
            while (it.hasNext()) {
                print("    insn", it.next());
            }
            for (LocalVariableNode localVariable : method.localVariables) {
                System.out.println("    localVariable " + localVariable.getClass().getSimpleName() + " " + localVariable.name + " " + localVariable.desc + " " + localVariable.signature);
            }
        }
    }

    private static void print(String prefix, Handle o) {
        System.out.println(prefix + " Handle " + o.getOwner() + " " + o.getName() + " " + o.getDesc());
    }

    private static void print(String prefix, Type o) {
        System.out.println(prefix + " Type " + o.getDescriptor());
    }

    private static void print(String prefix, AbstractInsnNode insn) {
        prefix = prefix + " " + insn.getClass().getSimpleName() + " " + getTypeName(insn.getType()) + " " + getOpcodeName(insn.getOpcode());
        BiConsumer<String, AbstractInsnNode> printer = printerMap.get(insn.getType());
        if (printer != null) {
            printer.accept(prefix, insn);
        } else {
            System.out.println(prefix);
        }
    }
}
