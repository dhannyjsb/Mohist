package red.mohist.util.perfomance;

import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;
import red.mohist.MohistConfig;

import java.util.TreeSet;

public class SubstitutionTreeSet<N> extends TreeSet
{

    private WorldServer worldServer;

    public SubstitutionTreeSet(WorldServer worldServer)
    {
        this.worldServer = worldServer;
    }

    @Override
    public boolean add(Object object)
    {
        this.worldServer.addPendingTickListEntriesTreeSetChunks((NextTickListEntry) object);
        return super.add(object);
    }

    @Override
    public boolean remove(Object object)
    {
        this.worldServer.removePendingTickListEntriesTreeSetChunks((NextTickListEntry) object);
        return super.remove(object);
    }

    @Override
    public void clear()
    {
        this.worldServer.clearPendingTickListEntriesTreeSetChunks();
        super.clear();
    }

    public static TreeSet<NextTickListEntry> getSubstitutionTreeSet(WorldServer worldServer)
    {

        if (MohistConfig.useChunksMapForPendingBlocks)
        {
            return new SubstitutionTreeSet<NextTickListEntry>(worldServer);
        }

        return new TreeSet<NextTickListEntry>();
    }
}
