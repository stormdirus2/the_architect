package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class UseItem {

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventUsage(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (TAPowers.AUROPHOBIA.isActive(user)) {
            ItemStack stack = user.getStackInHand(hand);
            if (stack != null) {
                if (ItemTags.PIGLIN_LOVED.contains(stack.getItem())) {
                    UseAction action = stack.getUseAction();
                    if (action == UseAction.EAT || action == UseAction.DRINK) {
                        cir.setReturnValue(TypedActionResult.fail(stack));
                    }
                }
            }
        }
    }
}
