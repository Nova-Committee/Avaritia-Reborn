package committee.nova.mods.avaritia.common.tile;

import committee.nova.mods.avaritia.api.common.item.BaseItemStackHandler;
import committee.nova.mods.avaritia.api.common.tile.BaseInventoryTileEntity;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import committee.nova.mods.avaritia.init.registry.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:36
 * @Description:
 */
public class InfinityChestTile extends BaseInventoryTileEntity implements LidBlockEntity {
    private final BaseItemStackHandler inventory;
    private final ChestLidController chestLidController = new ChestLidController();

    public InfinityChestTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.infinity_chest_tile.get(), pos, state);
        this.inventory = new BaseItemStackHandler(Integer.MAX_VALUE, this::setChangedAndDispatch);
    }

    public static void lidAnimateTick(Level pLevel, BlockPos pPos, BlockState pState, InfinityChestTile pBlockEntity) {
        pBlockEntity.chestLidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.chestLidController.shouldBeOpen(pType > 0);
            return true;
        } else {
            return super.triggerEvent(pId, pType);
        }
    }

    @Override
    public float getOpenNess(float pPartialTicks) {
        return this.chestLidController.getOpenness(pPartialTicks);
    }


    @Override
    public @NotNull BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.infinity_chest");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory) {
        return InfinityChestMenu.create(pContainerId, pPlayerInventory, this.inventory, this.getBlockPos());
    }
}
