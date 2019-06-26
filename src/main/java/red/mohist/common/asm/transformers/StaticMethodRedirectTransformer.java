package red.mohist.common.asm.transformers;

import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import red.mohist.common.asm.IAutoRegisterClassTransformer;

import java.util.*;

/**
 * 负责静态方法的重定向
 *
 * @author pyz
 * @date 2019/6/26 10:35 AM
 */
public class StaticMethodRedirectTransformer implements IAutoRegisterClassTransformer {
    private final Map<String, List<StaticMethodRedirect>> redirects = new HashMap<>();

    public StaticMethodRedirectTransformer() {
        register();
    }

    private void register() {
//        将所有Math.sin和Math.cos调用转换成MathHelper.sin和MathHelper.cos调用
        register(StaticMethodRedirect.builder()
                .fromType("java/lang/Math")
                .name("sin")
                .desc("(D)D")
                .toType("net/minecraft/util/math/MathHelper")
                .toName("sin")
                .ignoreFromTypes(Sets.newHashSet("net/minecraft/util/math/MathHelper"))
                .build());
        register(StaticMethodRedirect.builder()
                .fromType("java/lang/Math")
                .name("cos")
                .desc("(D)D")
                .toType("net/minecraft/util/math/MathHelper")
                .toName("cos")
                .ignoreFromTypes(Sets.newHashSet("net/minecraft/util/math/MathHelper"))
                .build());
    }

    private void register(StaticMethodRedirect rule) {
        List<StaticMethodRedirect> rules = redirects.get(rule.fromType);
        if (rules == null) {
            rules = new ArrayList<>();
            redirects.put(rule.fromType, rules);
        }
        rules.add(rule);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return basicClass;
        }
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        int count = 0;
        for (MethodNode m : classNode.methods) {
            if (redirectMethod(m)) {
                count++;
            }
        }
        if (count == 0) {
            return basicClass;
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private boolean redirectMethod(MethodNode methodNode) {
        int count = 0;
        for (ListIterator<AbstractInsnNode> it = methodNode.instructions.iterator(); it.hasNext(); ) {
            AbstractInsnNode insnNode = it.next();
            if (insnNode.getType() != AbstractInsnNode.METHOD_INSN) {
                continue;
            }
            MethodInsnNode node = (MethodInsnNode) insnNode;
            if (node.getOpcode() == Opcodes.INVOKESTATIC) {
                if (redirectMethod(node)) {
                    count++;
                }
            }
        }
        return count > 0;
    }

    private boolean redirectMethod(MethodInsnNode node) {
        List<StaticMethodRedirect> rules = redirects.get(node.owner);
        if (rules == null) {
            return false;
        }
        for (StaticMethodRedirect rule : rules) {
            if (rule.name.equals(node.name) && rule.desc.equals(node.desc)) {
                if (rule.getIgnoreFromTypes().contains(node.owner)) {
                    continue;
                }
                node.owner = rule.toType;
                node.name = rule.toName;
                return true;
            }
        }
        return false;
    }

    public static class StaticMethodRedirect {
        private String fromType;
        private String desc;
        private String name;
        private String toType;
        private String toName;
        private Set<String> ignoreFromTypes;

        private StaticMethodRedirect(Builder builder) {
            fromType = builder.fromType;
            desc = builder.desc;
            name = builder.name;
            toType = builder.toType;
            toName = builder.toName;
            ignoreFromTypes = builder.ignoreFromTypes;
            if (ignoreFromTypes == null) {
                ignoreFromTypes = new HashSet<>();
            }
            ignoreFromTypes.add(toType);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder newBuilder(StaticMethodRedirect copy) {
            Builder builder = new Builder();
            builder.fromType = copy.getFromType();
            builder.desc = copy.getDesc();
            builder.name = copy.getName();
            builder.toType = copy.getToType();
            builder.toName = copy.getToName();
            builder.ignoreFromTypes = copy.getIgnoreFromTypes();
            return builder;
        }


        public String getFromType() {
            return fromType;
        }

        public String getDesc() {
            return desc;
        }

        public String getName() {
            return name;
        }

        public String getToType() {
            return toType;
        }

        public String getToName() {
            return toName;
        }

        public Set<String> getIgnoreFromTypes() {
            return ignoreFromTypes;
        }

        /**
         * {@code StaticMethodRedirect} builder static inner class.
         */
        public static final class Builder {
            private String fromType;
            private String desc;
            private String name;
            private String toType;
            private String toName;
            private Set<String> ignoreFromTypes;

            private Builder() {
            }

            /**
             * Sets the {@code fromType} and returns a reference to this Builder so that the methods can be chained together.
             * @param fromType the {@code fromType} to set
             * @return a reference to this Builder
             */
            public Builder fromType(String fromType) {
                this.fromType = fromType;
                return this;
            }

            /**
             * Sets the {@code desc} and returns a reference to this Builder so that the methods can be chained together.
             * @param desc the {@code desc} to set
             * @return a reference to this Builder
             */
            public Builder desc(String desc) {
                this.desc = desc;
                return this;
            }

            /**
             * Sets the {@code name} and returns a reference to this Builder so that the methods can be chained together.
             * @param name the {@code name} to set
             * @return a reference to this Builder
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Sets the {@code toType} and returns a reference to this Builder so that the methods can be chained together.
             * @param toType the {@code toType} to set
             * @return a reference to this Builder
             */
            public Builder toType(String toType) {
                this.toType = toType;
                return this;
            }

            /**
             * Sets the {@code toName} and returns a reference to this Builder so that the methods can be chained together.
             * @param toName the {@code toName} to set
             * @return a reference to this Builder
             */
            public Builder toName(String toName) {
                this.toName = toName;
                return this;
            }

            /**
             * Sets the {@code ignoreFromTypes} and returns a reference to this Builder so that the methods can be chained together.
             * @param ignoreFromTypes the {@code ignoreFromTypes} to set
             * @return a reference to this Builder
             */
            public Builder ignoreFromTypes(Set<String> ignoreFromTypes) {
                this.ignoreFromTypes = ignoreFromTypes;
                return this;
            }

            /**
             * Returns a {@code StaticMethodRedirect} built from the parameters previously set.
             *
             * @return a {@code StaticMethodRedirect} built with parameters of this {@code StaticMethodRedirect.Builder}
             */
            public StaticMethodRedirect build() {
                return new StaticMethodRedirect(this);
            }
        }
    }
}
