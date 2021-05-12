package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 999)
public class Living extends BaseEntity {

    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    public void stopPushingAway(Entity entity, CallbackInfo ci) {
        //
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void stopPushingAwayFrom(Entity entity, CallbackInfo ci) {
        //
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void cannotTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isDimensionallyDisplacing(target)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "getAttributeValue",
            at = @At("RETURN"),
            cancellable = true
    )
    public void preventBonus(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        if (TAPowers.UNNATURAL_COMPOSITION.isActive((Entity) (Object) this)) {
            if (attribute == EntityAttributes.GENERIC_ARMOR || attribute == EntityAttributes.GENERIC_ARMOR_TOUGHNESS) {
                cir.setReturnValue(Math.min(Math.ceil(cir.getReturnValue() * 0.70), cir.getReturnValue()));
            }
        }
    }
}
