package committee.nova.mods.avaritia.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/21 12:49
 * @Description:
 */
public class StorageUtils {
    private static final HashMap<Item, String> ITEM_ID_MAP = new HashMap<>();
    private static final HashMap<String, Item> ID_ITEM_MAP = new HashMap<>();
    private static final HashMap<Fluid, String> FLUID_ID_MAP = new HashMap<>();
    private static final HashMap<String, Fluid> ID_FLUID_MAP = new HashMap<>();

    public static int sortFromCount(String s1, String s2, HashMap<String, Long> storageItems, boolean reverseOrder) {
        int i;
        if (reverseOrder) {
            i = storageItems.get(s2).compareTo(storageItems.get(s1));
        } else {
            i = storageItems.get(s1).compareTo(storageItems.get(s2));
        }
        if (i == 0) i = s1.compareTo(s2);
        return i;
    }

    public static int sortFromRightID(String s1, String s2) {
        int i = s1.indexOf(":");
        String a = s1.substring(i + 1);
        int j = s2.indexOf(":");
        String b = s2.substring(j + 1);
        int k = a.compareTo(b);
        if (k == 0) k = s1.compareTo(s2);
        return k;
    }

    public static int sortFromMirrorID(String s1, String s2) {
        char[] a = s1.toCharArray();
        char[] b = s2.toCharArray();
        int j = a.length - 1;
        int k = b.length - 1;
        int l;
        int min = Math.min(a.length, b.length);
        for (int i = 0; i < min; i++) {
            l = Character.compare(a[j], b[k]);
            if (l != 0) return l;
            j--; k--;
        }
        return Integer.compare(a.length, b.length);
    }


    public static String getItemId(Item item) {
        if (ITEM_ID_MAP.containsKey(item)) return ITEM_ID_MAP.get(item);
        else {
            String id = ForgeRegistries.ITEMS.getKey(item).toString();
            ITEM_ID_MAP.put(item, id);
            ID_ITEM_MAP.put(id, item);
            return id;
        }
    }

    public static Item getItem(String id) {
        if (ID_ITEM_MAP.containsKey(id)) return ID_ITEM_MAP.get(id);
        else {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item == null || item.equals(Items.AIR)) return Items.AIR;
            ID_ITEM_MAP.put(id, item);
            ITEM_ID_MAP.put(item, id);
            return item;
        }
    }

    public static String getFluidId(Fluid fluid) {
        if (FLUID_ID_MAP.containsKey(fluid)) return FLUID_ID_MAP.get(fluid);
        else {
            String id = ForgeRegistries.FLUIDS.getKey(fluid).toString();
            FLUID_ID_MAP.put(fluid, id);
            ID_FLUID_MAP.put(id, fluid);
            return id;
        }
    }

    public static Fluid getFluid(String id) {
        if (ID_FLUID_MAP.containsKey(id)) return ID_FLUID_MAP.get(id);
        else {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(id));
            if (fluid == null) return Fluids.EMPTY;
            ID_FLUID_MAP.put(id, fluid);
            FLUID_ID_MAP.put(fluid, id);
            return fluid;
        }
    }

    public static class Sort {
        public static final byte ID_ASCENDING = 0;
        public static final byte ID_DESCENDING = 1;
        public static final byte NAMESPACE_ID_ASCENDING = 2;
        public static final byte NAMESPACE_ID_DESCENDING = 3;
        public static final byte MIRROR_ID_ASCENDING = 4;
        public static final byte MIRROR_ID_DESCENDING = 5;
        public static final byte COUNT_ASCENDING = 6;
        public static final byte COUNT_DESCENDING = 7;
    }
    private static final class Action {
        public static final int LEFT_CLICK_DUMMY_SLOT = 0;
        public static final int Right_CLICK_DUMMY_SLOT = 1;
        public static final int LEFT_SHIFT_DUMMY_SLOT = 2;
        public static final int Right_SHIFT_DUMMY_SLOT = 3;
        public static final int THROW_ONE = 4;
        public static final int THROW_STICK = 5;
        public static final int LEFT_DRAG = 6;
        public static final int RIGHT_DRAG = 7;
        public static final int CLONE = 8;
        public static final int DRAG_CLONE = 9;
    }
}
