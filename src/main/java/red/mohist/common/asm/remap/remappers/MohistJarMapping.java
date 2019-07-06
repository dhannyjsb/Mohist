package red.mohist.common.asm.remap.remappers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MappingTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;
import red.mohist.MohistConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 维护nms->mcp的映射
 *
 * @author pyz
 * @date 2019/7/2 10:02 PM
 */
public class MohistJarMapping extends JarMapping implements ClassRemapperSupplier {
    private static final Logger LOGGER = LogManager.getLogger("MohistJarMapping");
    public final Map<String, String> reverseClasses = new HashMap<String, String>();
    /**
     * classInternalName.methodArgumentsDesc.methodName
     */
    public final Map<String, Map<String, Map<String, String>[]>> fastMethodMappings = new HashMap<>();
    public final Map<String, BiMap<String, String>> fastFieldMappings = new HashMap<>();

    public MohistJarMapping() {
    }

    @Override
    public void loadMappings(BufferedReader reader, MappingTransformer inputTransformer, MappingTransformer outputTransformer, boolean reverse) throws IOException {
        super.loadMappings(reader, inputTransformer, outputTransformer, reverse);
        this.classes.forEach((k, v) -> reverseClasses.put(v, k));
    }

    public void initFastMethodMapping(Remapper remapper) {
        for (Map.Entry<String, String> entry : this.methods.entrySet()) {
            String key = entry.getKey();
            int j = key.indexOf(' ');
            int i = key.lastIndexOf('/', j);
            String classInternalName = key.substring(0, i);
            String className = remapper.map(classInternalName).replace('/', '.');
            String methodName = key.substring(i + 1, j);
            String methodDesc = key.substring(j + 1);
            methodDesc = remapper.mapMethodDesc(methodDesc);
            Type[] ts = Type.getArgumentTypes(methodDesc);
            StringJoiner sj = new StringJoiner(",");
            for (Type t : ts) {
                sj.add(t.getClassName());
            }
            String methodArgumentsDesc = sj.toString();
            if (Objects.equals(methodName, entry.getValue())) {
                if (MohistConfig.printInvalidMapping) {
                    LOGGER.warn("fast mapping detect invalid mapping,ignore it:" + entry.getKey() + " -> " + entry.getValue());
                }
                continue;
            }
            Map<String, String>[] mapping = fastMethodMappings.computeIfAbsent(className, kk -> new HashMap<>())
                    .computeIfAbsent(methodArgumentsDesc, kk -> new Map[]{new HashMap(), new HashMap()});
            mapping[0].put(methodName, entry.getValue());
            mapping[1].put(entry.getValue(), methodName);
        }
        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            String key = entry.getKey();
            int dot = key.lastIndexOf('/');
            String classInternalName = key.substring(0, dot);
            String className = remapper.map(classInternalName).replace('/', '.');
            String fieldName = key.substring(dot + 1);
            if (Objects.equals(fieldName, entry.getValue())) {
                if (MohistConfig.printInvalidMapping) {
                    LOGGER.warn("fast mapping detect invalid mapping,ignore it:" + entry.getKey() + " -> " + entry.getValue());
                }
                continue;
            }
            fastFieldMappings.computeIfAbsent(className, kk -> HashBiMap.create())
                    .put(key.substring(dot + 1), entry.getValue());
        }
    }

    public String fastMapFieldName(Class clazz, String name) {
        return fastMapFieldName(false, clazz, name);
    }

    public String fastReverseMapFieldName(Class clazz, String name) {
        return fastMapFieldName(true, clazz, name);
    }

    public String fastMapMethodName(Class clazz, String name, Class... args) {
        return fastMapMethodName(false, clazz, name, args);
    }

    public String fastReverseMapMethodName(Class clazz, String name, Class... args) {
        return fastMapMethodName(true, clazz, name, args);
    }

    private String fastMapFieldName(boolean inverse, Class clazz, String name) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return name;
        }
        BiMap<String, String> map1 = fastFieldMappings.get(className);
        if (map1 == null) {
            return name;
        }
        if (inverse) {
            map1 = map1.inverse();
        }
        return map1.getOrDefault(name, name);
    }

    private String fastMapMethodName(boolean inverse, Class clazz, String name, Class... args) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return name;
        }
        Map<String, Map<String, String>[]> map1 = fastMethodMappings.get(className);
        if (map1 == null) {
            return name;
        }
        Map<String, String>[] map2 = map1.get(join(args));
        if (map2 == null) {
            return name;
        }
        int index = 0;
        if (inverse) {
            index = 1;
        }
        return map2[index].getOrDefault(name, name);
    }

    private String join(Class... args) {
        StringJoiner sj = new StringJoiner(",");
        for (Class arg : args) {
            sj.add(arg.getName());
        }
        return sj.toString();
    }

}
