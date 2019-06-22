package red.mohist.util.perfomance;

import com.google.common.collect.Lists;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;
import red.mohist.MohistConfig;

import java.util.ArrayList;
import java.util.TreeSet;

public class SubstitutionArrayList<N> extends ArrayList
{

    private WorldServer worldServer;

    public SubstitutionArrayList(WorldServer worldServer)
    {
        this.worldServer = worldServer;
    }

    @Override
    public boolean add(Object object)
    {
        worldServer.addPendingTickListEntriesThisTickChunks((NextTickListEntry) object);
        return super.add(object);
    }

    @Override
    public boolean remove(Object object)
    {
        worldServer.removePendingTickListEntriesThisTickChunks((NextTickListEntry) object);
        return super.remove(object);
    }

    @Override
    public void clear()
    {
        worldServer.clearPendingTickListEntriesThisTickChunks();
        super.clear();
    }

    public static ArrayList<NextTickListEntry> getSubstitutionArrayList(WorldServer worldServer)
    {

        if (MohistConfig.useChunksMapForPendingBlocks)
        {
            return new SubstitutionArrayList<NextTickListEntry>(worldServer);
        }

        return Lists.<NextTickListEntry>newArrayList();
    }
}
