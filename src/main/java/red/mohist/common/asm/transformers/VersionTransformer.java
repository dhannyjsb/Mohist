package red.mohist.common.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import red.mohist.Mohist;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pyz
 * @date 2019/6/30 7:08 AM
 */
public class VersionTransformer implements IClassTransformer {

    private final String targetVersion;
    private final Pattern cbPattern;
    private final Pattern nmsPattern;

    // net/minecraft/server/(v\d_\d+_R\d)/[\w/]+
    //    org/bukkit/craftbukkit/(v\d_\d+_R\d)/[\w/]+
// org/bukkit/craftbukkit/v1_10_R1/inventory/CraftItemStack;
//    Lorg/bukkit/craftbukkit/v1_10_R1/inventory/CraftItemStack<Lorg/bukkit/craftbukkit/v1_10_R1/inventory/CraftItemStack;Lorg/bukkit/craftbukkit/v1_10_R1/inventory/CraftItemStack;>
    public VersionTransformer() {
        cbPattern = Pattern.compile("org/bukkit/craftbukkit/(v\\d_\\d+_R\\d)/[\\w/]+");
        nmsPattern = Pattern.compile("net/minecraft/server/(v\\d_\\d+_R\\d)/[\\w/]+");
        targetVersion = Mohist.getNativeVersion();
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || name.startsWith("net.minecraft.server.") || name.startsWith("org.bukkit.")) {
            return basicClass;
        }
        ClassReader reader = new ClassReader(basicClass); // Turn from bytes into visitor
        ClassNode node = new ClassNode();
        reader.accept(node, 0); // Visit using ClassNode
        int modify = 0;
        modify += remapClass(node);
        if (node.fields != null) {
            for (FieldNode field : node.fields) {
                modify += remapField(field);
            }
        }
        if (node.methods != null) {
            for (MethodNode method : node.methods) {
                modify += remapMethod(method);
            }
        }
        if (modify == 0) {
            return basicClass;
        }
        System.out.println("remap " + transformedName);
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);

        {
            for (FieldNode field : node.fields) {
                System.out.println("FieldNode " + field.name + " " + field.desc);
            }
            for (MethodNode method : node.methods) {
                System.out.println("MethodNode " + method.name + " " + method.desc);
                ListIterator<AbstractInsnNode> it = method.instructions.iterator();
                while (it.hasNext()) {
                    AbstractInsnNode item = it.next();
                    if (item instanceof MethodInsnNode) {
                        MethodInsnNode n = (MethodInsnNode) item;
                        System.out.println("  MethodInsnNode " + n.owner + " " + n.name + " " + n.desc);
                    } else if (item instanceof TypeInsnNode) {
                        TypeInsnNode n = (TypeInsnNode) item;
                        System.out.println("  TypeInsnNode " + n.desc);
                    } else if (item instanceof InvokeDynamicInsnNode) {
                        InvokeDynamicInsnNode n = (InvokeDynamicInsnNode) item;
                        System.out.println("  InvokeDynamicInsnNode " + n.name + " " + n.desc + " " + n.bsm.getOwner() + " " + n.bsm.getName() + " " + n.bsm.getDesc());
                    } else if (item instanceof FieldInsnNode) {
                        FieldInsnNode n = (FieldInsnNode) item;
                        System.out.println("  FieldInsnNode " + n.desc);
                    } else {
                        System.out.println("  " + item.getClass().getSimpleName());
                    }
                }
                for (LocalVariableNode localVariable : method.localVariables) {
                    System.out.println("  LocalVariableNode " + localVariable.name + " " + localVariable.desc + " " + localVariable.signature);
                }
            }
        }
        basicClass = writer.toByteArray();
        try {
            Path dumpPath = Paths.get(System.getProperty("user.dir") + File.separator + "classDump" + File.separator + transformedName.replace('.', '/'));
            if (!dumpPath.toFile().exists()) {
                if (!dumpPath.toFile().getParentFile().exists()) {
                    dumpPath.toFile().getParentFile().mkdirs();
                }
                dumpPath.toFile().createNewFile();
            }
            Files.write(dumpPath, basicClass, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basicClass;
    }

    private int remapClass(ClassNode node) {
        int n = 0;
        n += remap(node.superName, str -> node.superName = str);
        if (node.interfaces != null && node.interfaces.size() > 0) {
            for (int i = 0; i < node.interfaces.size(); i++) {
                final int index = i;
                n += remap(node.interfaces.get(i), str -> node.interfaces.set(index, str));
            }
        }
        return n;
    }


    private int remapField(FieldNode node) {
        int n = 0;
        n += remap(node.desc, str -> node.desc = str);
        return n;
    }

    private int remapMethod(MethodNode node) {
        int n = 0;
        n += remapMethodDesc(node.desc, str -> node.desc = str);
        if (node.instructions != null) {
            ListIterator<AbstractInsnNode> it = node.instructions.iterator();
            while (it.hasNext()) {
                AbstractInsnNode insn = it.next();
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode item = (MethodInsnNode) insn;
                    n += remapMethodDesc(item.desc, str -> item.desc = str);
                    n += remap(item.owner, str -> item.owner = str);
                }
                if (insn instanceof InvokeDynamicInsnNode) {
                    InvokeDynamicInsnNode item = (InvokeDynamicInsnNode) insn;
                    n += remapMethodDesc(item.desc, str -> item.desc = str);
                    if (item.bsm != null) {
                        n += remapHandle(item.bsm, handle -> item.bsm = handle);
                    }
                    if (item.bsmArgs != null) {
                        for (int i = 0; i < item.bsmArgs.length; i++) {
                            int index = i;
                            Object bsmArg = item.bsmArgs[i];
                            if (bsmArg instanceof Type) {
                                n += remapType((Type) bsmArg, t -> item.bsmArgs[index] = t);
                            } else if (bsmArg instanceof Handle) {
                                n += remapHandle((Handle) bsmArg, handle -> item.bsmArgs[index] = handle);
                            }
                            System.out.println(bsmArg.getClass().getName());
                        }
                    }
                } else if (insn instanceof TypeInsnNode) {
                    TypeInsnNode item = ((TypeInsnNode) insn);
                    n += remap(item.desc, str -> item.desc = str);
                } else if (insn instanceof FieldInsnNode) {
                    FieldInsnNode item = ((FieldInsnNode) insn);
                    n += remap(item.desc, str -> item.desc = str);
                    n += remap(item.owner, str -> item.owner = str);
                }
            }
        }
        if (node.localVariables != null) {
            for (LocalVariableNode localVariable : node.localVariables) {
                n += remap(localVariable.desc, str -> localVariable.desc = str);
                n += remap(localVariable.signature, str -> localVariable.signature = str);
            }
        }
        return n;
    }

    private int remapType(Type type, Consumer<Type> setter) {
        int n = 0;
        String[] rs = new String[1];
        n += remap(type.getDescriptor(), str -> {
            rs[0] = str;
        });
        if (n > 0) {
            setter.accept(Type.getType(rs[0]));
        } else {
            setter.accept(type);
        }
        return n;
    }

    private int remapHandle(Handle handle, Consumer<Handle> setter) {
        int n = 0;
        String[] rs = new String[2];
        n += remap(handle.getOwner(), str -> rs[0] = str);
        n += remap(handle.getDesc(), str -> rs[1] = str);
        if (n == 0) {
            setter.accept(handle);
        } else {
            setter.accept(new Handle(handle.getTag(), rs[0], handle.getName(), rs[1], handle.isInterface()));
        }
        return n;
    }

    private int remapMethodDesc(String old, Consumer<String> setter) {
        String desc = old;
        int n = 0;
        Type r = Type.getReturnType(desc);
        Type[] args = Type.getArgumentTypes(desc);
        String[] ts = new String[args.length + 1];
        n += remap(r.getDescriptor(), str -> ts[0] = str);
        for (int i = 0; i < args.length; i++) {
            final int index = i;
            n += remap(args[i].getDescriptor(), str -> ts[index + 1] = str);
        }
        if (n > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (int i = 1; i < ts.length; i++) {
                sb.append(ts[i]);
            }
            sb.append(")");
            sb.append(ts[0]);
            String rs = sb.toString();
            System.out.println(old + " -> " + rs);
            setter.accept(sb.toString());
        } else {
            setter.accept(old);
        }
        return n;
    }

    private int remap(String old, Consumer<String> setter) {
        String str = old;
        int n = 0;
        if (str == null || str.isEmpty()) {
            setter.accept(old);
            return n;
        }
        Matcher m = cbPattern.matcher(old);
        if (m.find()) {
            String srcVersion = m.group(1);
            if (!Objects.equals(srcVersion, targetVersion)) {
                str = str.replace(m.group(1), targetVersion);
                n++;
            }
        }
        m = nmsPattern.matcher(old);
        if (m.find()) {
            String srcVersion = m.group(1);
            if (!Objects.equals(srcVersion, targetVersion)) {
                str = str.replace(m.group(1), targetVersion);
                n++;
            }
        }
        if (n > 0) {
            System.out.println(old + " -> " + str);
        }
        setter.accept(str);
        return n;
    }

}
