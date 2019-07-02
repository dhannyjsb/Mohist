package red.mohist.common.asm.remap.remappers;

import net.md_5.specialsource.provider.InheritanceProvider;
import red.mohist.common.asm.remap.RemapUtils;

import java.util.HashSet;
import java.util.Set;

public class MohistInheritanceProvider implements InheritanceProvider {
    @Override
    public Set<String> getParents(String className) {
        className = RemapUtils.map(className);
        try {
            Set<String> parents = new HashSet<String>();
            Class<?> reference = Class.forName(className.replace('/', '.').replace('$', '.'), false, Thread.currentThread().getContextClassLoader());
            Class<?> extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(RemapUtils.reverseMap(extend.getName().replace('.', '/')));
            }
            for (Class<?> inter : reference.getInterfaces()) {
                parents.add(RemapUtils.reverseMap(inter.getName().replace('.', '/')));
            }
            return parents;
        } catch (Exception e) {
        }
        return null;
    }

}