package committee.nova.mods.avaritia.common.net;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.iface.IFilterItem;
import committee.nova.mods.avaritia.init.registry.ModCaps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * C2SJEIGhostPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class C2SItemFilterPacket {
    private final int action;
    private final ItemStack stack;

    public C2SItemFilterPacket(FriendlyByteBuf buf) {
        this.action = buf.readInt();
        this.stack = buf.readItem();
    }

    public C2SItemFilterPacket(int action, ItemStack stack) {
        this.action = action;
        this.stack = stack;
    }

    public static void write(C2SItemFilterPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.action);
        buf.writeItem(msg.stack);
    }

    public static void run(C2SItemFilterPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            if (player.getMainHandItem().getItem() instanceof IFilterItem) {
                    switch(msg.action) {
                        case 0 -> {
                            CompoundTag filters = new CompoundTag();
                            filters.put(ForgeRegistries.ITEMS.getKey(msg.stack.getItem()).toString(), msg.stack.serializeNBT());
                            player.getMainHandItem().getOrCreateTag().put("filters", filters);
                        }
                        case 1 -> {
                            CompoundTag filters = player.getMainHandItem().getOrCreateTag().getCompound("filters");
                            if (filters.contains(ForgeRegistries.ITEMS.getKey(msg.stack.getItem()).toString())){
                                filters.remove(ForgeRegistries.ITEMS.getKey(msg.stack.getItem()).toString());
                            }
                        }
                        case 2 -> {
                            CompoundTag filters = player.getMainHandItem().getOrCreateTag().getCompound("filters");
                            filters.getAllKeys().forEach(filters::remove);
                        }
                    }

            }

        });
        ctx.get().setPacketHandled(true);
    }
}
