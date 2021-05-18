package net.fenrir.thearchitect.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = Entity.class, priority = 999)
public abstract class BaseEntity {

    @Shadow
    public World world;
    @Shadow
    @Final
    protected Random random;
    @Shadow
    protected boolean onGround;
    @Shadow
    private int fireTicks;

    @Shadow
    public abstract double getRandomBodyY();

    @Shadow
    public abstract double getParticleX(double widthScale);

    @Shadow
    public abstract double getParticleZ(double widthScale);

    @Environment(EnvType.CLIENT)
    @Shadow
    public abstract boolean isInvisibleTo(PlayerEntity player);

    @Shadow
    public abstract void setVelocity(double x, double y, double z);

    @Shadow
    public abstract Vec3d getVelocity();

    @Inject(method = "collides", at = @At("RETURN"), cancellable = true)
    public void preventTargeting(CallbackInfoReturnable<Boolean> info) {
        //
    }

    @Inject(method = "spawnSprintingParticles", at = @At("HEAD"), cancellable = true)
    public void preventSprintParticles(CallbackInfo ci) {
        //
    }

    @Inject(
            method = "isTouchingWater",
            at = @At("TAIL"),
            cancellable = true
    )
    public void noTouchingWater(CallbackInfoReturnable<Boolean> cir) {
        //Overridden
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

    @Inject(
            method = "move",
            at = @At("TAIL")
    )
    public void adjustOnGround(MovementType type, Vec3d movement, CallbackInfo ci) {
        //Overridden
    }

    @Inject(
            method = "isSwimming",
            at = @At("RETURN"),
            cancellable = true
    )
    public void noSwim(CallbackInfoReturnable<Boolean> cir) {

    }

}
