package red.mohist.common.asm.remap;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import red.mohist.Mohist;
import red.mohist.common.asm.remap.model.RemapContext;
import sun.reflect.Reflection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodType;
import java.util.*;

/**
 *
 * @author pyz
 * @date 2019/6/30 11:50 PM
 */
public class RemapUtils {

    public static final String nmsPrefix = "net.minecraft.server." + Mohist.getNativeVersion() + ".";
    public static final String mohistPrefix = "red.mohist.";
    private static BiMap<String, String> methodDescMappings = HashBiMap.create();
    private static Map<String, List<String>> inheriteMap = new HashMap<>();
    private static final List<IRemapTransformer> transformers = new ArrayList<>();
    public static final JarMapping jarMapping;

    static {
        for (IRemapTransformer iRemapTransformer : ServiceLoader.load(IRemapTransformer.class)) {
            transformers.add(iRemapTransformer);
        }
        transformers.sort(Comparator.comparingInt(IRemapTransformer::order));

        jarMapping = new JarMapping();
        try {
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/", "it/unimi/dsi/fastutil/");
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/jline/", "jline/");
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/joptsimple/", "joptsimple/");
            jarMapping.methods.put("org/bukkit/Bukkit/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "_INVALID_getOnlinePlayers");

            Map<String, String> relocations = new HashMap<String, String>();
            relocations.put("net.minecraft.server", "net.minecraft.server." + Mohist.getNativeVersion());

            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(Mohist.class.getClassLoader().getResourceAsStream("mappings/nms.srg"))),
                    new MavenShade(relocations),
                    null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        JointProvider provider = new JointProvider();
//        provider.add(new ClassInheritanceProvider());
//        provider.add(new ClassLoaderProvider(this));
//        RemapUtils.jarMapping.setFallbackInheritanceProvider(provider);
//        Transformer.init(RemapUtils.jarMapping, remapper);

        try {
            loadMethodDescMappings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String remapMethodDesc(String desc) {
        return methodDescMappings.getOrDefault(desc, desc);
    }

    public static byte[] remapFindClass(String name, String tName, byte[] bs) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bs);
        classReader.accept(classNode, 0);
        System.out.println("before remap " + classNode.name);
        printClassNode(classNode);
        RemapContext context = new RemapContext(name, tName, bs, classNode);
        for (IRemapTransformer transformer : transformers) {
            transformer.remap(context);
        }
        if (context.remapCount > 0) {
            System.out.println("after remap " + classNode.name);
            printClassNode(classNode);
            return context.getLastRemapCode();
        } else {
            return bs;
        }
    }

    public static String remapClassNameV1(String from) {
        String typeName = from.replace('.', '/');
        typeName = remapPackageV2(typeName);
        String r = jarMapping.classes.get(typeName);
        return r == null ? from : r.replace('/', '.');
    }

    private static String remapPackageV2(String from) {
        for (Map.Entry<String, String> entry : jarMapping.packages.entrySet()) {
            if (from.startsWith(entry.getKey())) {
                return entry.getValue() + from.substring(entry.getKey().length());
            }
        }
        return from;
    }

    public static String remapClassNameV2(String from) {
        String typeName = remapPackageV2(from);
        String r = jarMapping.classes.get(typeName);
        return r == null ? from : r;
    }

    public static String remapMethodName(Class clazz, String name, MethodType methodType) {
        Type[] ts = new Type[methodType.parameterCount()];
        System.arraycopy(methodType.parameterArray(), 0, ts, 0, ts.length);
        String desc = Type.getMethodDescriptor(Type.getType(methodType.returnType()), ts);
        return remapMethodNameV2(clazz.getName().replace('.', '/'), name, desc);
    }

    public static String remapMethodNameV2(String classNameV2, String name, String desc) {
        String key = classNameV2 + "/" + name + " " + desc;
        return jarMapping.methods.getOrDefault(key, name);
    }

    public static String remapFieldName(Class clazz, String fieldName) {
        String key = clazz.getName().replace('.', '/') + "/" + fieldName;
        return jarMapping.fields.getOrDefault(key, fieldName);
    }

    public static String remapFieldNameV2(String classNameV2, String fieldName) {
        String key = classNameV2 + "/" + fieldName;
        return jarMapping.fields.getOrDefault(key, fieldName);
    }

    public static Class getCallerClass() {
        return Reflection.getCallerClass(3);
    }

    public static ClassLoader getCallerClassLoder() {
        return Reflection.getCallerClass(3).getClassLoader();
    }

    public static String toMethodArgDesc(Class... classes) {
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Class aClass : classes) {
            sj.add(Type.getDescriptor(aClass));
        }
        return sj.toString();
    }

    public static String toMethodArgDesc(String methodDesc) {
        int index = methodDesc.indexOf(")");
        if (index < 0) {
            return methodDesc;
        }
        return methodDesc.substring(0, index);
    }

    public static void printClassNode(ClassNode classNode) {
        System.out.println("============ " + classNode.name + " ============");
        for (FieldNode field : classNode.fields) {
            System.out.println("  field " + field.desc + " " + field.name + " " + field.signature);
        }

        for (MethodNode method : classNode.methods) {
            System.out.println("  method " + method.name + " " + method.desc);
            ListIterator<AbstractInsnNode> it = method.instructions.iterator();
            while (it.hasNext()) {
                AbstractInsnNode item = it.next();
                if (item instanceof MethodInsnNode) {
                    MethodInsnNode n = (MethodInsnNode) item;
                    System.out.println("    insn " + n.getClass().getSimpleName() + " " + n.owner + " " + n.name + " " + n.desc);
                } else if (item instanceof TypeInsnNode) {
                    TypeInsnNode n = (TypeInsnNode) item;
                    System.out.println("    insn " + n.getClass().getSimpleName() + " " + n.desc);
                } else if (item instanceof InvokeDynamicInsnNode) {
                    InvokeDynamicInsnNode n = (InvokeDynamicInsnNode) item;
                    System.out.println("    insn " + n.getClass().getSimpleName() + " " + n.name + " " + n.desc);
                    print("      insn ", n.bsm);
                    if (n.bsmArgs != null) {
                        for (Object bsmArg : n.bsmArgs) {
                            print("      insn ", bsmArg);
                        }
                    }
                } else if (item instanceof FieldInsnNode) {
                    FieldInsnNode n = (FieldInsnNode) item;
                    System.out.println("    insn " + n.getClass().getSimpleName() + " " + n.owner + " " + n.name + " " + n.desc);
                } else if (item instanceof VarInsnNode) {
                    VarInsnNode n = (VarInsnNode) item;
                    System.out.println("    insn " + item.getClass().getSimpleName() + " " + n.var);
                } else {
                    System.out.println("    insn " + item.getClass().getSimpleName());
                }
            }
            for (LocalVariableNode localVariable : method.localVariables) {
                System.out.println("  insn " + localVariable.getClass().getSimpleName() + " " + localVariable.name + " " + localVariable.desc + " " + localVariable.signature);
            }
        }
    }

    private static void print(String prefix, Handle o) {
        System.out.println(prefix + " " + o.getClass().getSimpleName() + " " + o.getOwner() + " " + o.getName() + " " + o.getDesc());
    }

    private static void print(String prefix, Type o) {
        System.out.println(prefix + " " + o.getClass().getSimpleName() + " " + o.getDescriptor());
    }

    private static void print(String prefix, Object o) {
        if (o instanceof Handle) {
            print(prefix, (Handle) o);
        } else if (o instanceof Type) {
            print(prefix, (Type) o);
        } else {
            System.out.println(prefix + " " + o.getClass().getSimpleName());
        }
    }

    private static void loadMethodDescMappings() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Mohist.class.getClassLoader().getResourceAsStream("mappings/nms.srg")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int commentIndex = line.indexOf(35);
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
                if (!line.isEmpty()) {
                    if (!line.startsWith("MD: ")) {
                        continue;
                    }
                    String[] sp = line.split("\\s+");
                    String firDesc = sp[2];
                    String secDesc = sp[4];
                    methodDescMappings.put(firDesc, secDesc);
                }
            }
        }
    }
}
