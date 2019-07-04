package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.provider.InheritanceProvider;
import org.objectweb.asm.tree.ClassNode;
import red.mohist.common.asm.remap.RemapUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MohistInheritanceProvider implements InheritanceProvider {
    @Override
    public Set<String> getParents(String className) {
        if (className.startsWith("org/springframework/")) {
//            不处理spring的类
            return null;
        }
        return fineParents(className, true);
    }

    protected Set<String> fineParents(String className, boolean remap) {
        if (className.startsWith("org/springframework/")) {
//            不处理spring的类
            return null;
        }
        ClassNode cn = MohistClassRepo.getInstance().findClass(className);
        if (cn == null) {
            if (!remap) {
                return null;
            }
            String remapClassName = RemapUtils.map(className);
            if (Objects.equals(remapClassName, className)) {
                return null;
            }
            return fineParents(remapClassName, false);
        }
        Set<String> parents = new HashSet<>();
        if (cn.superName != null) {
            parents.add(RemapUtils.reverseMap(cn.superName));
        }
        if (cn.interfaces != null) {
            for (String anInterface : cn.interfaces) {
                parents.add(RemapUtils.reverseMap(anInterface));
            }
        }
        return parents.isEmpty() ? null : parents;
    }
}