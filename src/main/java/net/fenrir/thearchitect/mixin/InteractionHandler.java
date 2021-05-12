package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 999)
public class InteractionHandler {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "interactBlock", at = @At(value = "HEAD"), cancellable = true)
    private void preventBlockInteraction(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (PowerHelper.isDimensionallyDisplacing(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "interactItem", at = @At(value = "HEAD"), cancellable = true)
    private void preventItemInteraction(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (PowerHelper.isDimensionallyDisplacing(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "processBlockBreakingAction", at = @At(value = "HEAD"), cancellable = true)
    private void preventBreak(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "continueMining", at = @At(value = "HEAD"), cancellable = true)
    private void preventMining(BlockState state, BlockPos pos, int i, CallbackInfoReturnable<Float> cir) {
        if (PowerHelper.isDimensionallyDisplacing(player)) {
            cir.setReturnValue(0F);
        }
    }
}
