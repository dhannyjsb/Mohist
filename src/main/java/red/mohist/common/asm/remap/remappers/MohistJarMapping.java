package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MappingTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/7/2 10:02 PM
 */
public class MohistJarMapping extends JarMapping implements ClassRemapperSupplier {

    public final Map<String, String> reverseClasses = new HashMap<String, String>();

    public MohistJarMapping() {
    }

    @Override
    public void loadMappings(BufferedReader reader, MappingTransformer inputTransformer, MappingTransformer outputTransformer, boolean reverse) throws IOException {
        super.loadMappings(reader, inputTransformer, outputTransformer, reverse);
        this.classes.forEach((k, v) -> reverseClasses.put(v, k));
    }
}
