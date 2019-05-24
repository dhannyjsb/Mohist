package red.mohist.remapnms;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;
import red.mohist.Mohist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MappingLoader {

    /**
     * Load NMS Mapping
     *
     * @param jarMapping
     * @throws IOException
     */
    private static void loadNmsMappings(JarMapping jarMapping) throws IOException {
        Map<String, String> relocations = new HashMap<String, String>();
        relocations.put("net.minecraft.server", "net.minecraft.server." + Mohist.getNativeVersion());

        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/nms.srg"))),
                new MavenShade(relocations),
                null, false);
    }

    /**
     * Load CraftBukkit libs and NMS Mapping
     *
     * @return
     */
    public static JarMapping loadMapping() {
        JarMapping jarMapping = new JarMapping();
        try {
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/com/google/gson", "com/google/gson");
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil", "it/unimi/dsi/fastutil");
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/jline", "jline");
            jarMapping.packages.put("org/bukkit/craftbukkit/libs/joptsimple", "joptsimple");

            loadNmsMappings(jarMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarMapping;
    }
}
