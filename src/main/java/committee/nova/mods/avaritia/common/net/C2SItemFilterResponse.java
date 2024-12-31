package committee.nova.mods.avaritia.common.net;

import committee.nova.mods.avaritia.api.iface.IFilterItem;
import committee.nova.mods.avaritia.init.registry.ModCaps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

/**
 * C2SJEIGhostPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class C2SItemFilterResponse {
    private final List<ItemStack> stacks;

    public C2SItemFilterResponse(FriendlyByteBuf buf) {
        this.stacks = buf.readList(FriendlyByteBuf::readItem);
    }

    public C2SItemFilterResponse(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public static void write(C2SItemFilterResponse msg, FriendlyByteBuf buf) {
        buf.writeCollection(msg.stacks, FriendlyByteBuf::writeItem);
    }

    public static void run(C2SItemFilterResponse msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;

        });
        ctx.get().setPacketHandled(true);
    }
}
