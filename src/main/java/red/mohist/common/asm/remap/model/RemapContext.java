package red.mohist.common.asm.remap.model;

import org.objectweb.asm.tree.ClassNode;

/**
 *
 * @author pyz
 * @date 2019/7/1 9:30 PM
 */
public class RemapContext {
    private final ClassNode classNode;
    private final String name;
    private final String tName;
    private final byte[] originCode;
    private byte[] lastRemapCode;
    /**
     * 执行了多少次remap操作
     */
    public int remapCount;

    public RemapContext(String name, String tName, byte[] originCode, ClassNode classNode) {
        this.classNode = classNode;
        this.originCode = originCode;
        this.name = name;
        this.tName = tName;
    }

    public String getName() {
        return name;
    }

    public String gettName() {
        return tName;
    }

    public void setLastRemapCode(byte[] lastRemapCode) {
        this.lastRemapCode = lastRemapCode;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public byte[] getOriginCode() {
        return originCode;
    }

    public byte[] getLastRemapCode() {
        return lastRemapCode;
    }
}
