package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerInteractionManager.class, priority = 999)
public class ClientInteractionHandler {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "interactBlock", at = @At(value = "HEAD"), cancellable = true)
    private void preventBlockInteraction(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (PowerHelper.isDimensionallyDisplacing(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "attackBlock", at = @At(value = "HEAD"), cancellable = true)
    private void noAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isDimensionallyDisplacing(this.client.player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "updateBlockBreakingProgress",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ceaseBreaking(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isDimensionallyDisplacing(this.client.player)) {
            cir.setReturnValue(false);
        }
    }
}
