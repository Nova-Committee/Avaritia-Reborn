package committee.nova.mods.avaritia.common.tile;

import committee.nova.mods.avaritia.api.common.tile.BaseTileEntity;
import committee.nova.mods.avaritia.api.common.wrapper.ItemStackWrapper;
import committee.nova.mods.avaritia.api.common.tile.BaseInventoryTileEntity;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import committee.nova.mods.avaritia.init.registry.ModTileEntities;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:36
 * @Description:
 */
public class InfinityChestTile extends BaseTileEntity implements LidBlockEntity, MenuProvider {
    @Getter @Setter private String filter = "";
    @Getter @Setter private byte sortType = 4;
    private final InfinityChestWrapper inventory;
    private final ChestLidController chestLidController = new ChestLidController();

    public InfinityChestTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.infinity_chest_tile.get(), pos, state);
        this.inventory = new InfinityChestWrapper();
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
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.infinity_chest");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new InfinityChestMenu(pContainerId, pPlayer, this);
    }


    private LazyOptional<IItemHandler> capability = LazyOptional.of(this::getInventory);

    public @NotNull InfinityChestWrapper getInventory(){
        return inventory;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.getInventory().deserializeNBT(tag);
        if (tag.contains("filter")) filter = tag.getString("filter");
        if (tag.contains("sortType")) sortType = tag.getByte("sortType");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.merge(this.getInventory().serializeNBT());
        tag.putString("filter", filter);
        tag.putByte("sortType", sortType);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == ForgeCapabilities.ITEM_HANDLER) {
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.capability);
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.capability = LazyOptional.of(this::getInventory);
    }

}
