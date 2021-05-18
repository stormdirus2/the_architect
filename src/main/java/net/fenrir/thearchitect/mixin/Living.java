package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 999)
public abstract class Living extends BaseEntity {


    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract AttributeContainer getAttributes();

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
        //Overridden
    }

    //@ModifyVariable(
    //    method = "travel",
    //    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"),
    //    ordinal = 0
    //)
    public double modifyFall(double amount) {
        return amount;
        //Overridden
    }

    @Inject(
            method = "travel",
            at = @At("TAIL")
    )
    public void noFall(Vec3d movementInput, CallbackInfo ci) {
        //Overridden
    }

    @Inject(
            method = "isInSwimmingPose",
            at = @At("RETURN"),
            cancellable = true
    )
    public void noSwimmingPose(CallbackInfoReturnable<Boolean> cir) {
        //Overridden
    }

    @Inject(
            method = "jump",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noJump(CallbackInfo ci) {
        //Overridden
    }

}
