package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.provider.InheritanceProvider;
import org.objectweb.asm.tree.ClassNode;
import red.mohist.common.asm.remap.RemapUtils;

import java.util.HashSet;
import java.util.Set;

public class MohistInheritanceProvider implements InheritanceProvider {
    @Override
    public Set<String> getParents(String className) {
        Set<String> parents = new HashSet<>();
        try {
            if (!className.startsWith("net/minecraft/")) {
                ClassNode cn = MohistClassRepo.getInstance().findClass(className);
                if (cn != null) {
                    if (cn.superName != null) {
                        parents.add(cn.superName);
                    }
                    if (cn.interfaces != null) {
                        for (String anInterface : cn.interfaces) {
                            parents.add(anInterface);
                        }
                    }
                    return parents.isEmpty() ? null : parents;
                }
            }
            if (className.startsWith("net/minecraft/")) {
                className = RemapUtils.map(className);
            }
            Class<?> reference = Class.forName(className.replace('/', '.').replace('$', '.'), false, Thread.currentThread().getContextClassLoader());
            Class<?> extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(RemapUtils.reverseMap(extend.getName().replace('.', '/')));
            }
            for (Class<?> inter : reference.getInterfaces()) {
                parents.add(RemapUtils.reverseMap(inter.getName().replace('.', '/')));
            }
            return parents.isEmpty() ? null : parents;
        } catch (Exception e) {
            return null;
        }
    }

}