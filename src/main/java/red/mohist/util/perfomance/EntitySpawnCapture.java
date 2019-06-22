package red.mohist.util.perfomance;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class EntitySpawnCapture
{
    private WorldServer worldServer;
    private List<EntitySpawn> entitySpawns;

    public EntitySpawnCapture(WorldServer worldServer) {
        this.entitySpawns = Lists.newArrayList();
        this.worldServer = worldServer;
    }

    public void apply() {
        for (EntitySpawn snap : this.entitySpawns) {
            snap.apply();
        }
    }

    public void addEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (this.worldServer.restoringBlockSnapshots) {
            return;
        }
        this.entitySpawns.add(new EntitySpawn(entity, reason));
    }

    class EntitySpawn
    {
        private Entity entity;
        private CreatureSpawnEvent.SpawnReason reason;
        private boolean isApply;

        public EntitySpawn(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
            this.entity = entity;
            this.reason = reason;
        }

        public void apply() {
            if (!this.isApply) {
                EntitySpawnCapture.this.worldServer.addEntity(this.entity, this.reason);
                this.isApply = true;
            }
        }
    }
}
