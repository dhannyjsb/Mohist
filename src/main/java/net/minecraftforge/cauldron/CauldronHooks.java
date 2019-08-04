package net.minecraftforge.cauldron;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CauldronHooks {

    public static Map<Class<? extends TileEntity>, TileEntityCache> tileEntityCache = new HashMap<>();

    public static boolean canTileEntityTick(TileEntity tileEntity, World world)
    {
        /*
        if (tileEntity == null || world.tileEntityWorldConfig == null) {
            return false;
        }
        if (MinecraftServer.tileEntityConfig.skipTileEntityTicks.getValue())
        {
            TileEntityCache teCache = tileEntityCache.get(tileEntity.getClass());
            if (teCache == null)
            {
                String teConfigPath = tileEntity.getClass().getName().replace(".", "-");
                teConfigPath = teConfigPath.replaceAll("[^A-Za-z0-9\\-]", ""); // Fix up odd class names to prevent YAML errors
                teCache = new TileEntityCache(tileEntity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), teConfigPath, world.tileEntityWorldConfig.getBoolean(teConfigPath + ".tick-no-players", false), world.tileEntityWorldConfig.getInt(teConfigPath + ".tick-interval", 1));
                tileEntityCache.put(tileEntity.getClass(), teCache);
            }

            // Tick with no players near?
            if (!teCache.tickNoPlayers && !world.isActiveBlockCoord(tileEntity.getPos().getX(), tileEntity.getPos().getZ()))
            {
                return false;
            }

            // Skip tick interval
            if (teCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % teCache.tickInterval == 0L))
            {
                return true;
            }

            if(world.getChunkProvider() instanceof ChunkProviderServer) // Thermos - allow the server to tick tiles that are trying to unload
            {
                ChunkProviderServer cps = ((ChunkProviderServer)world.getChunkProvider());
                if(cps.chunksToUnload.contains(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getZ() >> 4))
                {
                    Chunk c = cps.getChunkIfLoaded(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getY() >> 4);
                    if(c != null)
                    {
                        if(c.lastAccessedTick < 2L)
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

         */
        return true;
    }


}
