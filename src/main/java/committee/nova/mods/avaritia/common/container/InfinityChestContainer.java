package committee.nova.mods.avaritia.common.container;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import committee.nova.mods.avaritia.util.StorageUtils.Sort;
import committee.nova.mods.avaritia.util.StorageUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/14 22:58
 * @Description:
 */
public class InfinityChestContainer extends SimpleContainer {
    public final ArrayList<String[]> sortedObject = new ArrayList<>();
    public final ArrayList<String[]> viewingObject = new ArrayList<>();
    public final ArrayList<String> formatCount = new ArrayList<>();
    public Level level;
    public String filter;
    public byte sortType;
    public boolean LShifting;
    protected ArrayList<String> sortedItems = new ArrayList<>();
    private double scrollTo = 0.0D;
    public final InfinityChestWrapper channel;

    public InfinityChestContainer(InfinityChestWrapper channel, Level level, String filter, byte sortType, boolean LShifting) {
        super(99);
        this.channel = channel;
        this.level = level;
        this.filter = filter;
        this.sortType = sortType;
        this.LShifting = LShifting;
    }

    public void onScrollTo(double scrollTo) {
        this.scrollTo = scrollTo;
        scrollOffset(0);
    }

    public double getScrollOn() {
        return scrollTo;
    }

    public void scrollOffset(int offset) {

        int i = (int) Math.ceil(sortedObject.size() / 11.0D);
        i -=  9;
        int j = Math.round(i * (float) scrollTo);
        if (offset != 0) {
            j += offset;
            j = Math.max(0, Math.min(i, j));
            scrollTo = (double) j / (double) i;
        }
        viewingObject.clear();
        viewingObject.addAll(sortedObject.subList(j * 11, Math.min(sortedObject.size(), j * 11 + (99))));

        updateDummySlots(true);
    }

    public double onMouseScrolled(boolean isUp) {
        if (isUp) scrollOffset(-1);
        else scrollOffset(1);
        return scrollTo;
    }

    public void refreshContainer(boolean fullUpdate) {
        if (!level.isClientSide) return;
        if ((fullUpdate || sortType >= 6) && !LShifting) {
            sortedItems = new ArrayList<>(channel.storageItems.keySet());
            if (!filter.isEmpty()) {
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<String> temp1 = new ArrayList<>();
                ArrayList<String> temp2 = new ArrayList<>();
                char head = filter.charAt(0);
                if (head == '*') {
                    String s = filter.substring(1);
                    for (String itemName : sortedItems) if (itemName.contains(s)) temp.add(itemName);
                } else if (head == '$') {
                    String s = filter.substring(1);
                    for (String itemName : sortedItems) {
                        ItemStack itemStack = new ItemStack(StorageUtils.getItem(itemName));
                        ArrayList<String> tags = new ArrayList<>();
                        itemStack.getTags().forEach(itemTagKey -> tags.add(itemTagKey.location().getPath()));
                        for (String tag : tags) {
                            if (tag.contains(s)) {
                                temp.add(itemName);
                                break;
                            }
                        }
                    }
                } else {
                    for (String itemName : sortedItems) {
                        if (itemName.contains(filter)) temp.add(itemName);
                        else {
                            ItemStack itemStack = new ItemStack(StorageUtils.getItem(itemName));
                            if (itemStack.getDisplayName().getString().toLowerCase().contains(filter))
                                temp.add(itemName);
                        }
                    }
                }
                sortedItems = temp;
            }
            switch (sortType) {
                case Sort.ID_ASCENDING -> {
                    sortedItems.sort(StorageUtils::sortFromRightID);
                }
                case Sort.ID_DESCENDING -> {
                    sortedItems.sort(Collections.reverseOrder(StorageUtils::sortFromRightID));
                }
                case Sort.NAMESPACE_ID_ASCENDING -> {
                    sortedItems.sort(String::compareTo);
                }
                case Sort.NAMESPACE_ID_DESCENDING -> {
                    sortedItems.sort(Collections.reverseOrder(String::compareTo));
                }
                case Sort.MIRROR_ID_ASCENDING -> {
                    sortedItems.sort(StorageUtils::sortFromMirrorID);
                }
                case Sort.MIRROR_ID_DESCENDING -> {
                    sortedItems.sort(Collections.reverseOrder(StorageUtils::sortFromMirrorID));
                }
                case Sort.COUNT_ASCENDING -> {
                    sortedItems.sort((s1, s2) -> StorageUtils.sortFromCount(s1, s2, channel.storageItems, false));
                }
                case Sort.COUNT_DESCENDING -> {
                    sortedItems.sort((s1, s2) -> StorageUtils.sortFromCount(s1, s2, channel.storageItems, true));
                }
            }
            onChangeObjects();
            return;
        }
        updateDummySlots(fullUpdate);
    }

    public void onChangeObjects() {
        sortedObject.clear();
        sortedItems.forEach(s -> sortedObject.add(new String[]{"item", s}));
        scrollOffset(0);
    }

    public void updateDummySlots(boolean fullUpdate) {
        formatCount.clear();
        for (int j = 0; j < (99); j++) {
            if (j < viewingObject.size() && viewingObject.get(j) != null) {
                String id = viewingObject.get(j)[1];
                //叠堆数为1避开原版的数字渲染
                if (fullUpdate) this.setItem(j, new ItemStack(StorageUtils.getItem(id)));
                long count;
                if (viewingObject.get(j)[0].equals("item")) {
                    if (channel.storageItems.containsKey(id)) {
                        count = channel.storageItems.get(id);
                    } else {
                        formatCount.add(j, "§c0");
                        continue;
                    }
                } else {
                    count = 0;
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
            } else this.setItem(j, ItemStack.EMPTY);
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }
}
