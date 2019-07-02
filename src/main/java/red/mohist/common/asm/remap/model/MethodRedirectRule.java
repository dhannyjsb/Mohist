package red.mohist.common.asm.remap.model;

/**
 * 定义一个remap规则
 * @author pyz
 * @date 2019/6/30 11:50 PM
 */
public class MethodRedirectRule {
    private final String owner;
    private final String descWithoutReturnType;
    private final String name;
    private final String remapOwner;

    public MethodRedirectRule(String owner, String name, String descWithoutReturnType, String remapOwner) {
        this.owner = owner;
        this.descWithoutReturnType = descWithoutReturnType;
        this.name = name;
        this.remapOwner = remapOwner;
    }

    public String getRemapOwner() {
        return remapOwner;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescWithoutReturnType() {
        return descWithoutReturnType;
    }

    public String getName() {
        return name;
    }
}
