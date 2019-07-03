package red.mohist.common.asm.remap;

import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.transformer.MavenShade;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import red.mohist.Mohist;
import red.mohist.MohistConfig;
import red.mohist.common.asm.remap.remappers.*;
import sun.reflect.Reflection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/6/30 11:50 PM
 */
public class RemapUtils {

    public static final String nmsPrefix = "net.minecraft.server.";
    public static final String mohistPrefix = "red.mohist.";
    public static final MohistJarMapping jarMapping;
    private static final List<Remapper> remappers = new ArrayList<>();

    static {
        jarMapping = new MohistJarMapping();
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/", "it/unimi/dsi/fastutil/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/jline/", "jline/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/joptsimple/", "joptsimple/");
        jarMapping.methods.put("org/bukkit/Bukkit/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "_INVALID_getOnlinePlayers");
        JointProvider provider = new JointProvider();
        provider.add(new MohistInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider(provider);

        Map<String, String> relocations = new HashMap<String, String>();
        relocations.put("net.minecraft.server", "net.minecraft.server." + Mohist.getNativeVersion());
        try {
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(Mohist.class.getClassLoader().getResourceAsStream("mappings/nms.srg"))),
                    new MavenShade(relocations),
                    null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        nms版本兼容
        remappers.add(new NMSVersionRemapper());
//        nms -> mcp
        MohistJarRemapper jarRemapper = new MohistJarRemapper(jarMapping);
        remappers.add(jarRemapper);
//        反射代理
        remappers.add(new ReflectRemapper());
//        初始化fast映射
        jarMapping.initFastMethodMapping(jarRemapper);
    }

    public static byte[] remapFindClass(byte[] bs) throws IOException {
        if (MohistConfig.printRemapPluginClass) {
            System.out.println("========= before remap ========= ");
            ASMUtils.printClass(bs);
        }
        for (Remapper remapper : remappers) {
            ClassReader reader = new ClassReader(bs);
            ClassWriter writer = new ClassWriter(0);
            ClassRemapper classRemapper;
            if (remapper instanceof ClassRemapperSupplier) {
                classRemapper = ((ClassRemapperSupplier) remapper).getClassRemapper(writer);
            } else {
                classRemapper = new ClassRemapper(writer, remapper);
            }
            reader.accept(classRemapper, 0);
            writer.visitEnd();
            bs = writer.toByteArray();
            if (MohistConfig.printRemapPluginClass) {
                System.out.println("========= after " + remapper.getClass().getSimpleName() + " remap ========= ");
                ASMUtils.printClass(bs);
            }
        }
        if (MohistConfig.dumpRemapPluginClass) {
            ASMUtils.dump(System.getProperty("user.dir") + File.separator + "dumpRemapPluginClass", bs);
        }
        return bs;
    }

    public static String map(String typeName) {
        typeName = mapPackage(typeName);
        return jarMapping.classes.getOrDefault(typeName, typeName);
    }

    public static String reverseMap(String typeName) {
        return jarMapping.reverseClasses.getOrDefault(typeName, typeName);
    }

    public static String mapPackage(String typeName) {
        for (Map.Entry<String, String> entry : jarMapping.packages.entrySet()) {
            String prefix = entry.getKey();
            if (typeName.startsWith(prefix)) {
                return entry.getValue() + typeName.substring(prefix.length());
            }
        }
        return typeName;
    }

    public static String remapMethodDesc(String methodDescriptor) {
        Type rt = Type.getReturnType(methodDescriptor);
        Type[] ts = Type.getArgumentTypes(methodDescriptor);
        rt = Type.getType(ASMUtils.toDescriptorV2(map(rt.getInternalName())));
        for (int i = 0; i < ts.length; i++) {
            ts[i] = Type.getType(ASMUtils.toDescriptorV2(map(ts[i].getInternalName())));
        }
        return Type.getMethodType(rt, ts).getDescriptor();
    }

    public static String remapMethodName(Class clazz, String name, MethodType methodType) {
        return remapMethodName(clazz, name, methodType.parameterArray());
    }

    public static String remapMethodName(Class type, String name, Class<?>... parameterTypes) {
        return jarMapping.fastMapMethodName(type, name, parameterTypes);
    }

    public static String remapFieldName(Class type, String fieldName) {
        return jarMapping.fastMapFieldName(type, fieldName);
    }

    public static ClassLoader getCallerClassLoder() {
        return Reflection.getCallerClass(3).getClassLoader();
    }
}
