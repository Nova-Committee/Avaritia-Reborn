package committee.nova.mods.avaritia.common.net;

import committee.nova.mods.avaritia.client.screen.ExtremeAnvilScreen;
import committee.nova.mods.avaritia.common.container.slot.ModCraftRecipeSlot;
import committee.nova.mods.avaritia.common.menu.ExtremeAnvilMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
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
public class C2SRenamePacket {
    private final String name;

    public C2SRenamePacket(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
    }

    public C2SRenamePacket(String name) {
        this.name = name;
    }

    public static void write(C2SRenamePacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name);
    }

    public static void run(C2SRenamePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            AbstractContainerMenu abstractcontainermenu = player.containerMenu;
            if (abstractcontainermenu instanceof ExtremeAnvilMenu anvilmenu) {
                anvilmenu.setItemName(msg.name);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
