package red.mohist.common.asm.remap;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.transformer.MavenShade;
import net.minecraft.server.MinecraftServer;
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
    public static final MohistJarMapping jarMapping;
    private static final List<Remapper> remappers = new ArrayList<>();

    static {
        jarMapping = new MohistJarMapping();
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

        JointProvider provider = new JointProvider();
        provider.add(new MohistInheritanceProvider());
        provider.add(new ClassLoaderProvider((net.minecraft.launchwrapper.LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader()));
        jarMapping.setFallbackInheritanceProvider(provider);

//        nms版本兼容
        remappers.add(new NMSVersionRemapper());
//        nms -> mcp
        remappers.add(new JarRemapper(jarMapping));
//        反射代理
        remappers.add(new ReflectRemapper());
    }

    public static byte[] remapFindClass(byte[] bs) throws IOException {
//        System.out.println("========= before remap ========= ");
//        ASMUtils.printClass(bs);
        for (Remapper remapper : remappers) {
            ClassReader reader = new ClassReader(bs);
            ClassWriter writer = new ClassWriter(0);
            ClassRemapper classRemapper;
            if (remapper instanceof ClassRemapperSupplier) {
                classRemapper = ((ClassRemapperSupplier) remapper).getClassRemapper(writer);
            } else {
                classRemapper = new ClassRemapper(writer, remapper);
            }
            reader.accept(classRemapper, ClassReader.EXPAND_FRAMES);
            writer.visitEnd();
            bs = writer.toByteArray();
//            System.out.println("========= after " + remapper.getClass().getSimpleName() + " remap ========= ");
//            ASMUtils.printClass(bs);
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

    public static String remapMethodDesc(String desc) {
        Type[] ts = Type.getArgumentTypes(desc);
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Type t : ts) {
            sj.add(map(t.getInternalName()));
        }
        return sj.toString();
    }

    public static String remapClassNameV2(String from) {
        String typeName = mapPackage(from);
        String r = jarMapping.classes.get(typeName);
        return r == null ? from : r;
    }

    public static String remapMethodName(Class clazz, String name, MethodType methodType) {
        return remapMethodNameV2(clazz.getName().replace('.', '/'), name, methodType.toMethodDescriptorString());
    }

    public static String remapMethodNameV2(String classNameV2, String name, String desc) {
        String key = classNameV2 + "/" + name + " " + desc;
        return jarMapping.methods.getOrDefault(key, name);
    }

    public static String remapFieldName(String classNameV2, String fieldName) {
        String key = classNameV2 + "/" + fieldName;
        return jarMapping.fields.getOrDefault(key, fieldName);
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
}
