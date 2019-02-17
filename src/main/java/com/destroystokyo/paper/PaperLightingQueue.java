package com.destroystokyo.paper;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import jdk.nashorn.internal.runtime.Timing;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayDeque;

class PaperLightingQueue {
    private static final long MAX_TIME = 57499999L;

    static void processQueue(final long curTime) {
        final long startTime = System.nanoTime();
        final long maxTickTime = 57499999L - (startTime - curTime);
        if (maxTickTime <= 0L) {
            return;
        }
        Label_0150:
        for (final World world : MinecraftServer.getServer().worlds) {
            if (!world.paperConfig.queueLightUpdates) {
                continue;
            }
            final ObjectCollection<Chunk> loadedChunks = ((WorldServer) world).getChunkProvider().id2ChunkMap.values();
            for (final Chunk chunk : loadedChunks.toArray(new Chunk[0])) {
                if (chunk.lightingQueue.processQueue(startTime, maxTickTime)) {
                    break Label_0150;
                }
            }
        }
    }

    private static boolean isOutOfTime(final long maxTickTime, final long startTime) {
        return startTime > 0L && System.nanoTime() - startTime > maxTickTime;
    }

    static class LightingQueue extends ArrayDeque<Runnable> {
        private final Chunk chunk;

        LightingQueue(final Chunk chunk) {
            this.chunk = chunk;
        }

        private boolean processQueue(final long startTime, final long maxTickTime) {
            if (this.isEmpty()) {
                return false;
            }
            if (isOutOfTime(maxTickTime, startTime)) {
                return true;
            }
            return false;
        }

        void processUnload() {
            if (!this.chunk.world.paperConfig.queueLightUpdates) {
                return;
            }
            this.processQueue(0L, 0L);
            final int radius = 1;
            for (int x = this.chunk.x - 1; x <= this.chunk.x + 1; ++x) {
                for (int z = this.chunk.z - 1; z <= this.chunk.z + 1; ++z) {
                    if (x != this.chunk.x || z != this.chunk.z) {
                        final Chunk neighbor = MCUtil.getLoadedChunkWithoutMarkingActive(this.chunk.world, x, z);
                        if (neighbor != null) {
                            neighbor.lightingQueue.processQueue(0L, 0L);
                        }
                    }
                }
            }
        }
    }
}
