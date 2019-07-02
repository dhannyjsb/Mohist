package red.mohist.common.asm.remap.model;

import org.objectweb.asm.Type;

/**
 *
 * @author pyz
 * @date 2019/7/2 10:38 AM
 */
public class OwnedMethodDesc extends NamedMethodDesc {
    protected String owner;

    public OwnedMethodDesc(String owner, String name, Type returnType, Type[] parameterTypes) {
        super(name, returnType, parameterTypes);
        this.owner = owner;
    }

    public static OwnedMethodDesc from(String owner, String name, Type methodType) {
        return new OwnedMethodDesc(owner, name, methodType.getReturnType(), methodType.getArgumentTypes());
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
