package red.mohist.common.asm.remap.model;

import org.objectweb.asm.Type;

/**
 *
 * @author pyz
 * @date 2019/7/2 10:38 AM
 */
public class MethodDesc {
    protected Type returnType;
    protected Type[] parameterTypes;

    public MethodDesc(Type returnType, Type[] parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public static MethodDesc from(Type methodType) {
        return new MethodDesc(methodType.getReturnType(), methodType.getArgumentTypes());
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String toDesc() {
        return Type.getMethodDescriptor(returnType, parameterTypes);
    }

    public Type toType() {
        return Type.getType(Type.getMethodDescriptor(returnType, parameterTypes));
    }
}
