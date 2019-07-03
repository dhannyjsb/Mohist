package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MappingTransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 维护nms->mcp的映射
 *
 * @author pyz
 * @date 2019/7/2 10:02 PM
 */
public class MohistJarMapping extends JarMapping implements ClassRemapperSupplier {

    public final Map<String, String> reverseClasses = new HashMap<String, String>();
    /**
     * classInternalName.methodName.methodArgumentsDesc
     */
    public final Map<String, Map<String, Map<String, String>>> fastMethodMappings = new HashMap<>();
    public final Map<String, Map<String, String>> fastFieldMappings = new HashMap<>();

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
            fastMethodMappings.computeIfAbsent(className, kk -> new HashMap<>())
                    .computeIfAbsent(methodName, kk -> new HashMap<>())
                    .put(methodArgumentsDesc, entry.getValue());
        }
        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            String key = entry.getKey();
            int dot = key.lastIndexOf('/');
            String classInternalName = key.substring(0, dot);
            String className = remapper.map(classInternalName).replace('/', '.');
            fastFieldMappings.computeIfAbsent(className, kk -> new HashMap<>())
                    .put(key.substring(dot + 1), entry.getValue());
        }
    }

    public String fastMapFieldName(Class clazz, String name) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return name;
        }
        Map<String, String> map1 = fastFieldMappings.get(className);
        if (map1 == null) {
            return name;
        }
        return map1.getOrDefault(name, name);
    }

    public String fastMapMethodName(Class clazz, String name, Class... args) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return name;
        }
        Map<String, Map<String, String>> map1 = fastMethodMappings.get(className);
        if (map1 == null) {
            return name;
        }
        Map<String, String> map2 = map1.get(name);
        if (map2 == null) {
            return name;
        }
        return map2.getOrDefault(join(args), name);
    }

    private String join(Class... args) {
        StringJoiner sj = new StringJoiner(",");
        for (Class arg : args) {
            sj.add(arg.getName());
        }
        return sj.toString();
    }

}
