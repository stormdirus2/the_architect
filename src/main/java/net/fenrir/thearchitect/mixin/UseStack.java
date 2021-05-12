package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.TheArchitect;
import net.fenrir.thearchitect.common.PowerHelper;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 999)
public class UseStack {
    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventUsage(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack != null) {
            if (PowerHelper.isDimensionallyDisplacing(user)) {
                cir.setReturnValue(TypedActionResult.fail(stack));
            }
            if (TAPowers.AUROPHOBIA.isActive(user)) {
                if (TheArchitect.GOLD.contains(stack.getItem())) {
                    UseAction action = stack.getUseAction();
                    if (action == UseAction.EAT || action == UseAction.DRINK) {
                        cir.setReturnValue(TypedActionResult.fail(stack));
                    }
                }
            }
        }
    }

    @Inject(
            method = "onStoppedUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventUsage2(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing(user)) {
            ci.cancel();
        }
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    public void preventUsage3(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack != null) {
            if (PowerHelper.isDimensionallyDisplacing(user)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Inject(
            method = "usageTick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventUsage4(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing(user)) {
            user.clearActiveItem();
            ci.cancel();
        }
    }

    @Inject(
            method = "finishUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventUsage5(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = user.getMainHandStack();
        if (stack != null) {
            if (PowerHelper.isDimensionallyDisplacing(user)) {
                cir.setReturnValue(stack);
            }
        }
    }

}
