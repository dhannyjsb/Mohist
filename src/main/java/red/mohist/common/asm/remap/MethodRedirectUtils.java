package red.mohist.common.asm.remap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;
import red.mohist.common.asm.remap.model.MethodRedirectRule;
import red.mohist.common.asm.remap.proxy.ProxyClass;
import red.mohist.common.asm.remap.proxy.ProxyClassLoader;
import red.mohist.common.asm.remap.proxy.ProxyMethodHandles_Lookup;
import red.mohist.common.asm.remap.proxy.ProxyMethodType;

import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author pyz
 * @date 2019/7/1 10:56 PM
 */
public class MethodRedirectUtils {
    public static Map<String, Map<String, Map<String, MethodRedirectRule>>> methodRedirectMapping = new HashMap<>();

    static {
//        注册重定向规则
        registerMethodRemapper("java/lang/Class", "forName", new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "forName", new Class[]{String.class, Boolean.TYPE, ClassLoader.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getField", new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getMethod", new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "getDeclaredMethod", new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper("java/lang/invoke/MethodType", "fromMethodDescriptorString", new Class[]{String.class, ClassLoader.class}, ProxyMethodType.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "unreflect", new Class[]{String.class, ClassLoader.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findSpecial", new Class[]{Class.class, String.class, MethodType.class, Class.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findStatic", new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findVirtual", new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/invoke/MethodHandles$Lookup", "findVirtual", new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandles_Lookup.class);
        registerMethodRemapper("java/lang/ClassLoader", "loadClass", new Class[]{String.class}, ProxyClassLoader.class);
        registerMethodRemapper("java/lang/ClassLoader", "<init>", new Class[]{}, ProxyClassLoader.class);

    }

    public static void registerMethodRemapper(String owner, String name, Class[] args, Class remapOwner) {
        Map<String, Map<String, MethodRedirectRule>> byName = methodRedirectMapping.computeIfAbsent(owner, k -> new HashMap<>());
        Map<String, MethodRedirectRule> byDesc = byName.computeIfAbsent(name, k -> new HashMap<>());
        String desc = RemapUtils.toMethodArgDesc(args);
        byDesc.put(RemapUtils.toMethodArgDesc(args), new MethodRedirectRule(owner, name, desc, remapOwner.getName().replace('.', '/')));
    }

    public static int redirectStatic(MethodInsnNode insn, Class remapClass) {
        return redirectStatic(insn, remapClass.getName().replace('.', '/'));
    }

    public static int redirectStatic(MethodInsnNode insn, String remapClassName) {
        return redirectStatic(insn, remapClassName, insn.name);
    }

    public static int redirectStatic(MethodInsnNode insn, Class remapClass, String remapName) {
        return redirectStatic(insn, remapClass.getName().replace('.', '/'), remapName);
    }

    /**
     *
     * @param insn
     * @param remapClassName 以/分隔的类名
     * @param remapName  代理的方法名
     */
    public static int redirectStatic(MethodInsnNode insn, String remapClassName, String remapName) {
        int n = 0;
        if (!Objects.equals(insn.owner, remapClassName) || !Objects.equals(insn.name, remapName)) {
            n++;
        }
        insn.owner = remapClassName;
        insn.name = remapName;
        return n;
    }

    public static int redirectVirtual(MethodInsnNode insn, Class remapClass) {
        return redirectVirtual(insn, remapClass.getName().replace('.', '/'));
    }

    public static int redirectVirtual(MethodInsnNode insn, String remapClassName) {
        return redirectVirtual(insn, remapClassName, insn.name);
    }

    public static int redirectVirtual(MethodInsnNode insn, Class remapClass, String remapName) {
        return redirectVirtual(insn, remapClass.getName().replace('.', '/'), remapName);
    }

    /**
     * 将实例方法调用转换成静态方法调用
     * 实例对象作为静态方法的第一个参数,剩余参数一次排开
     *
     * @param insn
     * @param remapClassName 以/分隔的类名
     * @param remapName  代理的方法名
     */
    public static int redirectVirtual(MethodInsnNode insn, String remapClassName, String remapName) {
        if (Objects.equals(insn.owner, remapClassName)) {
            return 0;
        }
        if (Objects.equals(insn.name, remapName)) {
            return 0;
        }
        int n = 0;
        Type returnType = Type.getReturnType(insn.desc);
        Type[] realArgs = Type.getArgumentTypes(insn.desc);
        Type[] args = new Type[realArgs.length + 1];
        args[0] = Type.getObjectType(insn.owner);
        for (int i = 0; i < realArgs.length; i++) {
            args[i + 1] = realArgs[i];
        }
        String remapDesc = Type.getMethodDescriptor(returnType, args);
        if (Objects.equals(insn.desc, remapDesc)) {
            n++;
        }

        insn.name = remapName;
        insn.owner = remapClassName;
        insn.desc = remapDesc;
        insn.setOpcode(Opcodes.INVOKESTATIC);
        return n;
    }
}
