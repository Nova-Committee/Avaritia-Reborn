package committee.nova.mods.avaritia.common.menu;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.common.menu.BaseMenu;
import committee.nova.mods.avaritia.common.container.InfinityChestContainer;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper.*;
import committee.nova.mods.avaritia.init.registry.ModMenus;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 01:51
 * @Description:
 */
public class InfinityChestMenu extends BaseMenu {
    public final List<SlotInfo> viewingItems = new ArrayList<>();
    public final List<String> formatCount = new ArrayList<>();

    protected final Player player;
    @Getter
    public InfinityChestWrapper chestWrapper;
    public InfinityChestContainer chestContainer;
    private double scrollTo = 0.0D;

    public InfinityChestMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, playerInventory.player,
                new InfinityChestWrapper());
}

    public InfinityChestMenu(int id, Inventory playerInventory, Player player, InfinityChestWrapper chestWrapper) {
        super(ModMenus.infinity_chest.get(), id);
        this.chestContainer = new InfinityChestContainer();
        this.chestWrapper = chestWrapper;
        this.player = player;
        addSlots(player, player.getInventory());
    }

    public void onScrollTo(double scrollTo) {
        this.scrollTo = scrollTo;
        scrollOffset(0);
    }

    public double getScrollOn() {
        return scrollTo;
    }

    public void scrollOffset(int offset) {

        int i = (int) Math.ceil(chestWrapper.getSlots() / 11.0D);
        i -=  9;
        int j = Math.round(i * (float) scrollTo);
        if (offset != 0) {
            j += offset;
            j = Math.max(0, Math.min(i, j));
            scrollTo = (double) j / (double) i;
        }
        viewingItems.clear();
        viewingItems.addAll(chestWrapper.stacks.subList(j * 11, Math.min(chestWrapper.getSlots(), j * 11 + (99))));

        updateDummySlots();
    }

    public void updateDummySlots() {
        formatCount.clear();
        for (int j = 0; j < 99; j++) {
            if (j < viewingItems.size() && viewingItems.get(j) != null) {
                SlotInfo id = viewingItems.get(j);
                //叠堆数为1避开原版的数字渲染
                chestContainer.setItem(j, id.stack());
                long count;
                    if (chestWrapper.getStacks().contains(id)) {
                        count = id.count();
                    } else {
                        formatCount.add(j, "§c0");
                        continue;
                    }
                if (count < 1000L) formatCount.add(j, String.valueOf(count));
                else if (count < Long.MAX_VALUE) {
                    String stringCount = Static.DECIMAL_FORMAT.format(count);
                    stringCount = stringCount.substring(0, 4);
                    if (stringCount.endsWith(",")) stringCount = stringCount.substring(0, 3);
                    stringCount = stringCount.replace(",", ".");
                    if (count < 1000000L) stringCount += "K";
                    else if (count < 1000000000L) stringCount += "M";
                    else if (count < 1000000000000L) stringCount += "G";
                    else if (count < 1000000000000000L) stringCount += "T";
                    else if (count < 1000000000000000000L) stringCount += "P";
                    else stringCount += "E";
                    formatCount.add(j, stringCount);
                    // 9,223,372,036,854,775,807L
                    // e  p   t   g   m   k
                } else formatCount.add(j, "MAX");
            } else chestContainer.setItem(j, ItemStack.EMPTY);
        }
    }

    public double onMouseScrolled(boolean isUp) {
        if (isUp) scrollOffset(-1);
        else scrollOffset(1);
        return scrollTo;
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
        //虚拟储存物品格36 ~ 134
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                this.addSlot(new Slot(chestContainer, i * 11 + j, 6 + j * 17, 6 + i * 17));
            }
        }
    }

}
