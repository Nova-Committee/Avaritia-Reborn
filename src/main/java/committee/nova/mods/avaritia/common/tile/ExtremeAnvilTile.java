package committee.nova.mods.avaritia.common.tile;

import committee.nova.mods.avaritia.api.common.tile.BaseInventoryTileEntity;
import committee.nova.mods.avaritia.api.common.wrapper.ItemStackWrapper;
import committee.nova.mods.avaritia.common.menu.ExtremeAnvilMenu;
import committee.nova.mods.avaritia.init.registry.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/23 23:43
 * @Description:
 */
public class ExtremeAnvilTile extends BaseInventoryTileEntity {
    private static final Component CONTAINER_TITLE = Component.translatable("container.repair");
    public final ItemStackWrapper inventory;
    public SimpleContainerData data = new SimpleContainerData(1);
    private int exps;
    private boolean stopped = false;
    public ExtremeAnvilTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.extreme_anvil.get(), pos, state);
        this.inventory = createInventoryHandler(null);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ExtremeAnvilTile tile) {
        if (level.isClientSide) return;
        if (tile.data.get(0) < 100) {
            tile.stopped = false;
        }
        if (!tile.stopped) {
            tile.data.set(0, ++tile.exps);
        }
        if (tile.data.get(0) >= 100) {
            tile.stopped = true;
            tile.setChangedAndDispatch();
        }
    }
    public static ItemStackWrapper createInventoryHandler(Runnable onContentsChanged) {
        var inventory = new ItemStackWrapper(3, onContentsChanged);
        inventory.setOutputSlots(0);
        return inventory;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.exps = tag.getInt("exps");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("exps", exps);
    }

    @Override
    public @NotNull ItemStackWrapper getInventory() {
        return this.inventory;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new ExtremeAnvilMenu(pContainerId, pInventory, ContainerLevelAccess.create(pInventory.player.level(), getBlockPos()), this.data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return CONTAINER_TITLE;
    }
}
