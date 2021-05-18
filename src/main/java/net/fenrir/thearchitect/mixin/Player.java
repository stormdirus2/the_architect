package net.fenrir.thearchitect.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenrir.thearchitect.TheArchitect;
import net.fenrir.thearchitect.common.PowerHelper;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 999)
public abstract class Player extends Living {


    @Override
    public void preventTargeting(CallbackInfoReturnable<Boolean> ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.setReturnValue(false);
        }
    }

    @Override
    public void stopPushingAwayFrom(Entity entity, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noAttack(Entity target, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "interact",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noInteract(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(
            method = "clipAtLedge",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noClipAtLedge(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "spawnParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noParticles(ParticleEffect parameters, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "getNextLevelExperience",
            at = @At("RETURN"),
            cancellable = true
    )
    public void experienceIncrement(CallbackInfoReturnable<Integer> cir) {
        if (TAPowers.PERITIA_EFFICIENCY.isActive((PlayerEntity) (Object) this)) {
            cir.setReturnValue(cir.getReturnValue() / 5);
        }
    }

    @Override
    public void preventBonus(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        if (TAPowers.UNNATURAL_COMPOSITION.isActive((PlayerEntity) (Object) this)) {
            if (attribute == EntityAttributes.GENERIC_ARMOR || attribute == EntityAttributes.GENERIC_ARMOR_TOUGHNESS) {
                cir.setReturnValue(getAttributes().getValue(attribute) * 0.7);
            }
        }
    }

    @Override
    public void preventSprintParticles(CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Override
    public void stopPushingAway(Entity entity, CallbackInfo ci) {
        if (PowerHelper.isDimensionallyDisplacing((PlayerEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Override
    public void adjustOnGround(MovementType type, Vec3d movement, CallbackInfo ci) {
        if (PowerHelper.isSpatialStriding((PlayerEntity) (Object) this)) {
            this.onGround = true;
        }
    }

    @Override
    public double modifyFall(double amount) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            return 0;
        }
        return amount;
    }

    @Override
    public void noFall(Vec3d movementInput, CallbackInfo ci) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            Vec3d velocity = getVelocity();
            setVelocity(velocity.x, 0, velocity.z);
        }
    }

    @Override
    public void noSwimmingPose(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void noSwim(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void noTouchingWater(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHelper.isSpatialStriding((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void noJump(CallbackInfo ci) {
        if (TAPowers.ARCHITECTS_FOCUS.isActive((PlayerEntity) (Object) this) && getMainHandStack().getItem() == TheArchitect.ARCHITECTS_FOCUS) {
            ci.cancel();
        }
    }

}
