package committee.nova.mods.avaritia.common.tile;

import committee.nova.mods.avaritia.api.common.item.BaseItemStackHandler;
import committee.nova.mods.avaritia.api.common.tile.BaseInventoryTileEntity;
import committee.nova.mods.avaritia.common.menu.ModCraftMenu;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModCraftTier;
import committee.nova.mods.avaritia.init.registry.ModTileEntities;
import committee.nova.mods.avaritia.util.lang.Localizable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 8:44
 * Version: 1.0
 */
public class ModCraftTile extends BaseInventoryTileEntity {

    private final BaseItemStackHandler inventory;
    private ModCraftTier tier;

    public ModCraftTile(BlockPos pos, BlockState blockState) {
        super(ModTileEntities.mod_craft_tile.get(), pos, blockState);
        if (blockState.is(ModBlocks.sculk_crafting_table.get())) {
            tier = ModCraftTier.SCULK;
        } else if (blockState.is(ModBlocks.nether_crafting_table.get())) {
            tier = ModCraftTier.NETHER;
        } else if (blockState.is(ModBlocks.end_crafting_table.get())) {
            tier = ModCraftTier.END;
        } else if (blockState.is(ModBlocks.extreme_crafting_table.get())) {
            tier = ModCraftTier.EXTREME;
        }
        this.inventory = new BaseItemStackHandler(tier.size * tier.size, this::setChangedAndDispatch);
    }

    @Override
    public @NotNull BaseItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Localizable.of("container." + tier.name).build();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory) {
        switch (tier) {
            case SCULK -> {
                return ModCraftMenu.sculk(pContainerId, pInventory, this.inventory, this.getBlockPos(), ModCraftTier.SCULK);
            }
            case END -> {
                return ModCraftMenu.end(pContainerId, pInventory, this.inventory, this.getBlockPos(), ModCraftTier.END);
            }
            case NETHER -> {
                return ModCraftMenu.nether(pContainerId, pInventory, this.inventory, this.getBlockPos(), ModCraftTier.NETHER);
            }
            case EXTREME -> {
                return ModCraftMenu.extreme(pContainerId, pInventory, this.inventory, this.getBlockPos(), ModCraftTier.EXTREME);
            }
        }
        return ModCraftMenu.extreme(pContainerId, pInventory, this.inventory, this.getBlockPos(), ModCraftTier.EXTREME);
    }

}
