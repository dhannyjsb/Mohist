package red.mohist.common.asm.remap.model;

import org.objectweb.asm.Type;

/**
 *
 * @author pyz
 * @date 2019/7/2 10:38 AM
 */
public class NamedMethodDesc extends MethodDesc {
    protected String name;

    public NamedMethodDesc(String name, Type returnType, Type[] parameterTypes) {
        super(returnType, parameterTypes);
        this.name = name;
    }

    public static NamedMethodDesc from(String name, Type methodType) {
        return new NamedMethodDesc(name, methodType.getReturnType(), methodType.getArgumentTypes());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
