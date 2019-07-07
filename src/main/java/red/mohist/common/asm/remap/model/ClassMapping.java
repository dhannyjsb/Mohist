package red.mohist.common.asm.remap.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/7/7 12:04 PM
 */
public class ClassMapping {
    private String nmsSrcName;
    private String nmsInternalName;
    private String nmsSimpleName;
    private String nmsName;
    private String mcpSrcName;
    private String mcpInternalName;
    private String mcpName;
    private String mcpSimpleName;
    private final BiMap<String, String> fieldMapping = HashBiMap.create();
    /**
     * args nmsMethodName mcpMethodName
     */
    private final Map<String, Map<String, String>> methodMapping = new HashMap<>();
    /**
     * args mcpMethodNam nmsMethodName
     */
    private final Map<String, Map<String, String>> inverseMethodMapping = new HashMap<>();
    /**
     * args nmsMethodName mcpMethodName
     */
    private final Map<String, Map<String, String>> srcMethodMapping = new HashMap<>();
    /**
     * args mcpMethodNam nmsMethodName
     */
    private final Map<String, Map<String, String>> inverseSrcMethodMapping = new HashMap<>();

    public void setNmsSrcName(String nmsSrcName) {
        this.nmsSrcName = nmsSrcName;
        this.nmsInternalName = nmsSrcName.intern();
        int dot = this.nmsInternalName.lastIndexOf('$');
        if (dot > 0) {
            this.nmsSimpleName = this.nmsInternalName.substring(dot + 1).intern();
        } else {
            this.nmsSimpleName = this.nmsInternalName.substring(this.nmsInternalName.lastIndexOf('/') + 1).intern();
        }
        this.nmsName = this.nmsInternalName.replace('/', '.').intern();
    }

    public void setMcpSrcName(String mcpSrcName) {
        this.mcpSrcName = mcpSrcName;
        this.mcpInternalName = mcpSrcName;
        int dot = this.mcpInternalName.lastIndexOf('$');
        if (dot > 0) {
            this.mcpSimpleName = this.mcpInternalName.substring(dot + 1).intern();
        } else {
            this.mcpSimpleName = this.mcpInternalName.substring(this.mcpInternalName.lastIndexOf('/') + 1).intern();
        }
        this.mcpName = this.mcpInternalName.replace('/', '.').intern();
    }

    public Map<String, Map<String, String>> getSrcMethodMapping() {
        return srcMethodMapping;
    }

    public Map<String, Map<String, String>> getInverseSrcMethodMapping() {
        return inverseSrcMethodMapping;
    }

    public String getNmsSrcName() {
        return nmsSrcName;
    }

    public String getMcpSrcName() {
        return mcpSrcName;
    }

    public String getNmsInternalName() {
        return nmsInternalName;
    }

    public String getNmsSimpleName() {
        return nmsSimpleName;
    }

    public String getNmsName() {
        return nmsName;
    }

    public String getMcpInternalName() {
        return mcpInternalName;
    }

    public String getMcpName() {
        return mcpName;
    }

    public String getMcpSimpleName() {
        return mcpSimpleName;
    }

    public BiMap<String, String> getFieldMapping() {
        return fieldMapping;
    }

    public Map<String, Map<String, String>> getMethodMapping() {
        return methodMapping;
    }

    public Map<String, Map<String, String>> getInverseMethodMapping() {
        return inverseMethodMapping;
    }
}
