package red.mohist.common.asm.remap.remappers;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.common.asm.remap.RemapUtils;
import red.mohist.common.asm.remap.model.MethodRedirectRule;
import red.mohist.common.asm.remap.proxy.ProxyClass;
import red.mohist.common.asm.remap.proxy.ProxyClassLoader;
import red.mohist.common.asm.remap.proxy.ProxyMethodHandles_Lookup;
import red.mohist.common.asm.remap.proxy.ProxyMethodType;

import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:51 PM
 */
public class ReflectMethodRemapper extends MethodRemapper {
    private static Map<String, Map<String, Map<String, MethodRedirectRule>>> methodRedirectMapping = new HashMap<>();

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

    private static void registerMethodRemapper(String owner, String name, Class[] args, Class remapOwner) {
        Map<String, Map<String, MethodRedirectRule>> byName = methodRedirectMapping.computeIfAbsent(owner, k -> new HashMap<>());
        Map<String, MethodRedirectRule> byDesc = byName.computeIfAbsent(name, k -> new HashMap<>());
        String desc = RemapUtils.toMethodArgDesc(args);
        byDesc.put(RemapUtils.toMethodArgDesc(args), new MethodRedirectRule(owner, name, desc, remapOwner.getName().replace('.', '/')));
    }


    public ReflectMethodRemapper(MethodVisitor mv, Remapper remapper) {
        super(mv, remapper);
    }

    public ReflectMethodRemapper(int api, MethodVisitor mv, Remapper remapper) {
        super(api, mv, remapper);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (opcode == Opcodes.NEW && "java/net/URLClassLoader".equals(type)) {
            type = ProxyClassLoader.desc;
        }
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (Opcodes.INVOKESTATIC == opcode) {
            redirectVirtual(opcode, owner, name, desc, itf);
        } else if (Opcodes.INVOKEVIRTUAL == opcode) {
            redirectStatic(opcode, owner, name, desc, itf);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    private void redirectVirtual(int opcode, String owner, String name, String desc, boolean itf) {
        try {
            Map<String, Map<String, MethodRedirectRule>> byOwner = methodRedirectMapping.get(owner);
            if (byOwner == null) {
                return;
            }
            Map<String, MethodRedirectRule> byName = byOwner.get(name);
            if (byName == null) {
                return;
            }
            MethodRedirectRule rule = byName.get(desc);
            if (rule == null) {
                return;
            }
            owner = rule.getRemapOwner();
            opcode = Opcodes.INVOKESTATIC;
        } finally {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    private void redirectStatic(int opcode, String owner, String name, String desc, boolean itf) {
        try {
            Map<String, Map<String, MethodRedirectRule>> byOwner = methodRedirectMapping.get(owner);
            if (byOwner == null) {
                return;
            }
            Map<String, MethodRedirectRule> byName = byOwner.get(name);
            if (byName == null) {
                return;
            }
            MethodRedirectRule rule = byName.get(desc);
            if (rule == null) {
                return;
            }
            owner = rule.getRemapOwner();
        } finally {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

}
