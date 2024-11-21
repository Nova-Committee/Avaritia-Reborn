package committee.nova.mods.avaritia.common.menu;

import committee.nova.mods.avaritia.api.common.slot.FakeSlot;
import committee.nova.mods.avaritia.api.common.menu.BaseMenu;
import committee.nova.mods.avaritia.common.container.InfinityChestContainer;
import committee.nova.mods.avaritia.common.tile.InfinityChestTile;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import committee.nova.mods.avaritia.init.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 01:51
 * @Description:
 */
public class InfinityChestMenu extends BaseMenu {

    public final InfinityChestWrapper channel;
    protected final Player player;
    private final Level level;
    private final BlockPos blockPos;
    public InfinityChestTile controlPanelBlock;
    public InfinityChestContainer dummyContainer;

    public String filter;
    public byte sortType;
    public boolean LShifting = false;


    public InfinityChestMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenus.infinity_chest.get(), id);
        this.level = playerInventory.player.level();
        this.player = playerInventory.player;
        this.blockPos = extraData.readBlockPos();
        this.filter = extraData.readUtf(64);
        this.sortType = extraData.readByte();

        addSlots(playerInventory.player, playerInventory);
        this.channel = new InfinityChestWrapper();
        this.dummyContainer = new InfinityChestContainer(this.channel, this.level, this.filter, this.sortType, this.LShifting);
        //虚拟储存物品格51 ~ 149
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                this.addSlot(new FakeSlot(dummyContainer, i * 11 + j, 6 + j * 17, 6 + i * 17));
            }
        }
    }

    public InfinityChestMenu(int id, Player player, InfinityChestTile blockEntity) {
        super(ModMenus.infinity_chest.get(), id);
        this.level = player.level();
        this.player = player;
        this.blockPos = blockEntity.getBlockPos();
        this.controlPanelBlock = blockEntity;
        this.filter = blockEntity.getFilter();
        this.sortType = blockEntity.getSortType();
        this.channel = blockEntity.getInventory();
        addSlots(player, player.getInventory());
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean clickMenuButton(Player pPlayer, int pId) {
        switch (pId) {
            case 0 -> nextSort();
            case 1 -> reverseSort();
        }
        return pId < 2;
    }

    private void pushToInventory(ItemStack itemStack) {
        moveItemStackTo(itemStack, 9, 36, false);
        if (!itemStack.isEmpty()) moveItemStackTo(itemStack, 0, 9, false);
        if (!itemStack.isEmpty()) player.drop(itemStack, false);
    }

    private void savePushToInventory(ItemStack itemStack) {
        int loops = itemStack.getCount() / itemStack.getMaxStackSize();
        for (int i = 0; i < loops; i++) {
            ItemStack newStack = itemStack.copy();
            newStack.setCount(itemStack.getMaxStackSize());
            itemStack.setCount(itemStack.getCount() - itemStack.getMaxStackSize());
            pushToInventory(newStack);
        }
        if (itemStack.getCount() > 0) pushToInventory(itemStack);
    }

    private void fillStackFromInventory(ItemStack stack) {
        int maxStackSize = stack.getMaxStackSize();
        for (int i = 9; i < 36; i++) {
            applyNbtFromInvIndex(stack, i);
            if (stack.getCount() >= maxStackSize) break;
        }
        if (stack.getCount() < maxStackSize) for (int i = 0; i < 9; i++) {
            applyNbtFromInvIndex(stack, i);
            if (stack.getCount() >= maxStackSize) break;
        }
    }

    private void applyFromInvIndex(ItemStack itemStack, int slotId) {
        if (itemStack.getItem().equals(player.getInventory().getItem(slotId).getItem())) {
            ItemStack otherStack = player.getInventory().getItem(slotId);
            int needAmount = itemStack.getMaxStackSize() - itemStack.getCount();
            if (otherStack.getCount() > needAmount) {
                itemStack.setCount(itemStack.getMaxStackSize());
                otherStack.setCount(otherStack.getCount() - needAmount);
            }
            else {
                itemStack.setCount(itemStack.getCount() + otherStack.getCount());
                player.getInventory().setItem(slotId, ItemStack.EMPTY);
            }
        }
    }

    private void applyNbtFromInvIndex(ItemStack itemStack, int slotId) {
        if (ItemStack.isSameItemSameTags(itemStack, player.getInventory().getItem(slotId))) {
            ItemStack otherStack = player.getInventory().getItem(slotId);
            int needAmount = itemStack.getMaxStackSize() - itemStack.getCount();
            if (otherStack.getCount() > needAmount) {
                itemStack.setCount(itemStack.getMaxStackSize());
                otherStack.setCount(otherStack.getCount() - needAmount);
            }
            else {
                itemStack.setCount(itemStack.getCount() + otherStack.getCount());
                player.getInventory().setItem(slotId, ItemStack.EMPTY);
            }
        }
    }

    public void nextSort() {
        sortType += 2;
        if (sortType > 7) sortType %= 8;
        if (level.isClientSide) dummyContainer.refreshContainer(true);
    }

    public void reverseSort() {
        if (sortType % 2 == 0) sortType++;
        else sortType--;
        if (level.isClientSide) dummyContainer.refreshContainer(true);
    }

    private void saveBlock() {
        controlPanelBlock.setFilter(filter);
        controlPanelBlock.setSortType(sortType);
    }

    private void addSlots(Player player, Inventory playerInv) {
        //快捷栏0~8
        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInv, l, 23 + l * 17, 227));
        }

        //背包9~35
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 23 + i1 * 17, 176 + k * 17));
            }
        }
    }

}
