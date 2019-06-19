package red.mohist.util;

import net.minecraft.tileentity.TileEntityHopper;

public class HopperTask
{
    public final TileEntityHopper hopper;
    public final long time;
    
    public HopperTask(final TileEntityHopper hopper, final long time) {
        this.hopper = hopper;
        this.time = time;
    }
}
