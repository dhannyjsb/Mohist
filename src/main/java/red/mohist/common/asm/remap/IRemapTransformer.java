package red.mohist.common.asm.remap;

import red.mohist.common.asm.remap.model.RemapContext;

/**
 *
 * @author pyz
 * @date 2019/7/1 9:17 PM
 */
public abstract class IRemapTransformer {

    /**
     * 每次执行remap操作后,都要将context中的计数器+1
     * 返回null表示没有进行转换
     * @return
     */
    protected abstract byte[] doRemap(RemapContext context);

    public void remap(RemapContext context) {
        if (!canRemap(context)) {
            return;
        }
        byte[] bs = doRemap(context);
        if (bs != null) {
            context.setLastRemapCode(bs);
        }
    }

    protected boolean canRemap(RemapContext context) {
        return true;
    }

    /**
     * 升序
     * @return
     */
    protected int order() {
        return 0;
    }
}
