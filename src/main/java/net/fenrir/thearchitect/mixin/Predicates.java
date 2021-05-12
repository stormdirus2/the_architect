package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPredicates.class, priority = 999)
public class Predicates {
    @Dynamic("Lambda method injection")
    @Inject(method = {"method_5910", "method_24517"}, at = @At("RETURN"), cancellable = true)
    private static void exceptCreativeOrSpectator(@Nullable Entity tested, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValueZ() && tested != null && PowerHelper.isDimensionallyDisplacing(tested)) {
            info.setReturnValue(false);
        }
    }
}
