package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PreventArmorBonus {

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
