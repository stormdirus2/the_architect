package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.TheArchitect;
import net.fenrir.thearchitect.common.PowerHelper;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherPlayer extends Player {


    @Inject(at = @At("HEAD"), method = "tick")
    public void spawnParticles(CallbackInfo ci) {
        Entity player = (Entity) (Object) this;
        if (!this.isInvisibleTo(MinecraftClient.getInstance().player)) {
            if (isAlive() && !PowerHelper.isDimensionallyDisplacing(player) && TAPowers.ARCHITECTS_PARTICLE.isActive(player)) {
                world.addParticle((ParticleEffect) TheArchitect.ARCHITECTS_PARTICLE, getParticleX(0.5D), getRandomBodyY() - 0.25D, getParticleZ(0.5D), (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }
}
