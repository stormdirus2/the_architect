package net.fenrir.thearchitect.common;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RiftHelper {

    public static void createRift(World world, BlockPos pos) {
        MinecraftServer server = world.getServer();
        if (server != null) {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                if (player.world == world) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(pos);
                    ServerPlayNetworking.send(player, TheArchitect.ARCHITECT_RIFT, buf);
                }
            });
        }
    }
}
