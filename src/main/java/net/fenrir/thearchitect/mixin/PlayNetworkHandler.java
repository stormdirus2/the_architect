package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 999)
public class PlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerInteractEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;interact(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    private void interactEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo info) {
        verifyInteract(packet, info);
    }

    private void verifyInteract(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        World world = player.getEntityWorld();
        Entity entity = packet.getEntity(world);
        if (PowerHelper.isDimensionallyDisplacing(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "onPlayerInteractEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;interactAt(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", shift = At.Shift.BEFORE), cancellable = true)
    private void interactEntity2(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        verifyInteract(packet, ci);
    }
}
