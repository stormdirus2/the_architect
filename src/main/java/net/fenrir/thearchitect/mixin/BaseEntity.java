package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 999)
public class BaseEntity {

    @Shadow
    private int fireTicks;

    @Inject(method = "collides", at = @At("RETURN"), cancellable = true)
    public void preventTargeting(CallbackInfoReturnable<Boolean> info) {
        //
    }

    @Inject(method = "spawnSprintingParticles", at = @At("HEAD"), cancellable = true)
    public void preventSprintParticles(CallbackInfo ci) {
        //
    }

    @Inject(
            method = "setFireTicks",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventFire(int ticks, CallbackInfo ci) {
        if (TAPowers.NONFLAMMABLE.isActive((net.minecraft.entity.Entity) (Object) this)) {
            this.fireTicks = 0;
            ci.cancel();
        }
    }
}
