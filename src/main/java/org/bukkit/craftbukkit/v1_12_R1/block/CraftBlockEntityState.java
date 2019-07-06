package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState<T> {

    private final Class<T> tileEntityClass;
    private T tileEntity;
    private T snapshotTileEntity;

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);
        this.tileEntityClass = tileEntityClass;
    }

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material, tileEntity);
        this.tileEntityClass = (Class<T>) tileEntity.getClass();
    }

    // gets the wrapped TileEntity
    @Override
    public T getTileEntity() { // Paper - protected -> public
        load();
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        load();
        return snapshotTileEntity;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();
        return captureTileEntityFromWorld();
    }

    // gets the NBT data of the TileEntity represented by this block state
    public NBTTagCompound getSnapshotNBT() {
        return super.getSnapshotNBT();
    }

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        if (tileEntity == null) {
            return;
        }
        if (tileEntity == snapshotTileEntity) {
            return;
        }
        captureSnapshotFromTileEntity(tileEntity);
    }

    /**
     * 用指定的TileEntity更新快照数据
     * 保留快照坐标
     *
     * @param tileEntity
     */
    protected void captureSnapshotFromTileEntity(T tileEntity) {
        this.tileEntity = tileEntity;
        super.captureSnapshotFromTileEntity(tileEntity);
        BlockPos pos = snapshotTileEntity.getPos();
        snapshotTileEntity.readFromNBT(getSnapshotNBT());
        snapshotTileEntity.setPos(pos);
    }

    /**
     * 用快照更新指定的TileENtity
     * @param tileEntity
     */
    protected void updateTileEntityBySnapshot(T tileEntity) {
        BlockPos pos = tileEntity.getPos();
        tileEntity.readFromNBT(getSnapshotNBT());
        tileEntity.setPos(pos);
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        load();
        if (tileEntity != null && tileEntity != getSnapshot()) {
            updateTileEntityBySnapshot(tileEntity);
        }
    }

    protected boolean isApplicable(TileEntity tileEntity) {
        return tileEntityClass.isInstance(tileEntity);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            TileEntity tile = getTileEntityFromWorld();

            if (isApplicable(tile)) {
                applyTo(tileEntityClass.cast(tile));
                tile.markDirty();
            }
        }

        return result;
    }

    /**
     * 从世界中读取快照
     */
    @Override
    protected void captureSnapshotFromWorld() {
        captureSnapshotTileEntityFromWorld();
    }

    /**
     * 从world中取tileEntity
     */
    @Override
    protected T captureTileEntityFromWorld() {
        tileEntity = (T) super.captureTileEntityFromWorld();
        return tileEntity;
    }

    /**
     * 从world中获取当前位置的快照TileEntity
     * @return
     */
    protected T captureSnapshotTileEntityFromWorld() {
        NBTTagCompound nbt = captureSnapshotNBTFromWorld();
        if (nbt == null) {
            snapshotTileEntity = null;
            return null;
        }
        snapshotTileEntity = (T) TileEntity.create(tileEntity.getWorld(), nbt);
        return snapshotTileEntity;
    }

}
