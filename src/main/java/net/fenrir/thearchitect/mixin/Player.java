package net.fenrir.thearchitect.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 999)
public class Player extends Living {

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

}
