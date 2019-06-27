package red.mohist;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import red.mohist.common.asm.CompositeClassTransformer;

import javax.annotation.Nullable;
import java.util.Map;

public class CorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                CompositeClassTransformer.class.getCanonicalName()
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
