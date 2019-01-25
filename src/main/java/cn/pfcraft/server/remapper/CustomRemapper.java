package cn.pfcraft.server.remapper;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

public class CustomRemapper extends JarRemapper {
    public CustomRemapper(JarMapping jarMapping){
        super(jarMapping);
    }

    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }
}
