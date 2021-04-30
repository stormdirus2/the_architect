package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.ArchitectsCurseAccessor;
import net.fenrir.thearchitect.common.PlayerInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBulletEntity.class)
public abstract class ArchitectsCurse extends Entity implements ArchitectsCurseAccessor {

    @Shadow
    private Entity target;
    @Shadow
    private double targetX;
    @Shadow
    private double targetY;
    @Shadow
    private double targetZ;
    private LivingEntity attacking;
    @Shadow
    private int stepCount;
    @Shadow
    private Direction direction;
    private int ticksAsBullet = 0;
    private int attackCooldown;

    public ArchitectsCurse(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract void setDirection(Direction direction);

    @Shadow
    protected abstract void method_7486(@Nullable Direction.Axis axis);

    @Inject(
            method = "onBlockHit",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ignoreBlock(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (isCurse()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onCollision",
            at = @At(value = "INVOKE", ordinal = 1),
            cancellable = true
    )
    public void noRemove(HitResult hitResult, CallbackInfo ci) {
        if (getActive()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onEntityHit",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onlyHitTarget(EntityHitResult entityHitResult, CallbackInfo ci) {
        this.attacking = null;
        if (this.isCurse()) {
            if (entityHitResult.getEntity() != target || attackCooldown > 0) {
                ci.cancel();
            } else {
                Entity result = entityHitResult.getEntity();
                if (result instanceof LivingEntity) {
                    attacking = (LivingEntity) result;
                }
            }
        }
    }

    @ModifyArg(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
            )
    )
    public StatusEffectInstance changeEffect(StatusEffectInstance original) {
        if (this.isCurse()) {
            return new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    60
            );
        }
        return original;
    }

    @ModifyArg(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    public float modifyDamage(float original) {
        if (isCurse()) {
            return 1F;
        } else {
            return original;
        }
    }

    @Inject(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    public void ignoreIframes(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (attacking != null) {
            attacking.timeUntilRegen = 0;
        }
    }

    @Inject(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
            )
    )
    public void noIframes(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (attacking != null) {
            attackCooldown = 5;
            attacking.timeUntilRegen = 0;
        }
    }

    @ModifyArg(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            )
    )
    public ParticleEffect replaceParticle(ParticleEffect original) {
        return this.isCurse() ? ParticleTypes.COMPOSTER : original;
    }

    @Inject(
            method = "method_7486",
            at = @At("HEAD"),
            cancellable = true
    )
    public void overridePathfinding(Direction.Axis axis, CallbackInfo ci) {
        if (isCurse()) {
            BlockPos blockPos2;
            if (target == null) {
                blockPos2 = getBlockPos().down();
            } else {
                blockPos2 = new BlockPos(target.getX(), target.getY(), target.getZ());
            }

            double e = (double) blockPos2.getX() + 0.5D;
            double f = (double) blockPos2.getY() + target.getHeight() / 2 + 0.25;
            double g = (double) blockPos2.getZ() + 0.5D;
            setDirection(null);
            double h = e - this.getX();
            double j = f - this.getY();
            double k = g - this.getZ();

            this.targetX = Math.min(h, 5) / 8;
            this.targetY = Math.min(j, 5) / 8;
            this.targetZ = Math.min(k, 5) / 8;

            this.velocityDirty = true;
            this.stepCount = 20;
            ci.cancel();
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void moreSpeed(CallbackInfo ci) {
        if (getActive()) {
            ticksAsBullet++;
            if (attackCooldown > 0) {
                attackCooldown--;
            }
            if (ticksAsBullet >= 200) {
                this.remove();
            }
            method_7486(direction == null ? null : direction.getAxis());
            Vec3d vec3d = getVelocity();
            this.setVelocity(vec3d.add((targetX - vec3d.x) * 0.3D, (targetY - vec3d.y) * 0.3D, (targetZ - vec3d.z) * 0.3D));
            if (this.getVelocity().squaredDistanceTo(Vec3d.ZERO) < 0.01 * target.getWidth()) {
                this.setVelocity(this.getVelocity().multiply(2 + 5 * Math.sqrt(target.getWidth())));
            }
            updatePosition(getX() + vec3d.x, getY() + vec3d.y, getZ() + vec3d.z);
        }
    }

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ownerNoHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() == ((ProjectileEntity) (Object) this).getOwner()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "readCustomDataFromTag",
            at = @At("TAIL")
    )
    public void readSaveData(CompoundTag tag, CallbackInfo ci) {
        Entity newOwner = ((ProjectileEntity) (Object) this).getOwner();
        if (newOwner instanceof PlayerEntity) {
            ((PlayerInterface) newOwner).addCurse((ShulkerBulletEntity) (Object) this);
        }
    }

    public boolean getActive() {
        return isCurse() && target != null && target.isAlive() && ((ProjectileEntity) (Object) this).getOwner() != null;
    }


    @Override
    public boolean isCurse() {
        ShulkerBulletEntity shulkerBulletEntity = (ShulkerBulletEntity) (Object) this;
        return shulkerBulletEntity.hasCustomName() && shulkerBulletEntity.getCustomName().getString().equals("Architect's Curse");
    }

    @Override
    public Entity getTarget() {
        return this.target;
    }
}
