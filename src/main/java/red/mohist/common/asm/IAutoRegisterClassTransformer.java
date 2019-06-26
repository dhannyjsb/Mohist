package red.mohist.common.asm;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * 利用java spi实现自动注册
 *
 * @author pyz
 * @date 2019/6/26 11:11 AM
 */
public interface IAutoRegisterClassTransformer extends IClassTransformer {
    /**
     * 升序排列
     *
     * @return
     */
    default int order() {
        return 0;
    }
}
