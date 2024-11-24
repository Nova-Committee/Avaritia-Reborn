package committee.nova.mods.avaritia.common.net;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.container.slot.ModCraftRecipeSlot;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * C2SJEIGhostPacket
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/28 14:02
 */
public class C2SICFilterPacket {
    private final int containerId;
    private final String filter;

    public C2SICFilterPacket(FriendlyByteBuf buf) {
        this.containerId = buf.readInt();
        this.filter = buf.readUtf(64);
    }

    public C2SICFilterPacket(int containerId, String filter) {
        this.containerId = containerId;
        this.filter = filter;
    }

    public static void write(C2SICFilterPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.containerId);
        buf.writeUtf(msg.filter, 64);
    }

    public static void run(C2SICFilterPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            if (player.containerMenu.containerId == msg.containerId) {
                if (!player.containerMenu.stillValid(player)) {
                    Static.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
                } else {
                    //((InfinityChestMenu) player.containerMenu).filter = msg.filter;
                    player.containerMenu.broadcastChanges();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
