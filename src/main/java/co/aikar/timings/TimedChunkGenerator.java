package co.aikar.timings;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.generator.InternalChunkGenerator;
import org.bukkit.generator.BlockPopulator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TimedChunkGenerator extends InternalChunkGenerator {
    private final WorldServer world;
    private final InternalChunkGenerator timedGenerator;

    public TimedChunkGenerator(final WorldServer worldServer, final InternalChunkGenerator gen) {
        this.world = worldServer;
        this.timedGenerator = gen;
    }

    @Deprecated
    @Override
    public byte[] generate(final World world, final Random random, final int x, final int z) {
        return this.timedGenerator.generate(world, random, x, z);
    }

    @Deprecated
    @Override
    public short[][] generateExtBlockSections(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return this.timedGenerator.generateExtBlockSections(world, random, x, z, biomes);
    }

    @Deprecated
    @Override
    public byte[][] generateBlockSections(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return this.timedGenerator.generateBlockSections(world, random, x, z, biomes);
    }

    @Override
    public ChunkData generateChunkData(final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
        return this.timedGenerator.generateChunkData(world, random, x, z, biome);
    }

    @Override
    public boolean canSpawn(final World world, final int x, final int z) {
        return this.timedGenerator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        return this.timedGenerator.getDefaultPopulators(world);
    }

    @Override
    public Location getFixedSpawnLocation(final World world, final Random random) {
        return this.timedGenerator.getFixedSpawnLocation(world, random);
    }

    @Override
    public net.minecraft.world.chunk.Chunk generateChunk(int x, int z) {
        try (final Timing ignored = this.world.timings.chunkGeneration.startTiming()) {
            return this.timedGenerator.generateChunk(x, z);
        }
    }

    @Override
    public void populate(int x, int z) {
        try (final Timing ignored = this.world.timings.syncChunkLoadStructuresTimer.startTiming()) {
            this.timedGenerator.populate(x, z);
        }
    }

    @Override
    public boolean generateStructures(net.minecraft.world.chunk.Chunk chunkIn, int x, int z) {
        return this.timedGenerator.generateStructures(chunkIn, x, z);
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.timedGenerator.getPossibleCreatures(creatureType, pos);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(net.minecraft.world.World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return this.timedGenerator.getNearestStructurePos(world, structureName, position, findUnexplored);
    }

    @Override
    public void recreateStructures(net.minecraft.world.chunk.Chunk chunkIn, int x, int z) {
        try (final Timing ignored = this.world.timings.syncChunkLoadStructuresTimer.startTiming()) {
            this.timedGenerator.recreateStructures(chunkIn, x, z);
        }
    }

    @Override
    public boolean isInsideStructure(net.minecraft.world.World worldIn, String structureName, BlockPos pos) {
        return this.timedGenerator.isInsideStructure(world, structureName, pos);
    }
}