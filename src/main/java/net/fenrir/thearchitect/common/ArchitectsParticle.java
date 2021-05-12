package net.fenrir.thearchitect.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class ArchitectsParticle extends SpriteBillboardParticle {
    private final double startX;
    private final double startY;
    private final double startZ;

    protected ArchitectsParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        velocityX = g;
        velocityY = h;
        velocityZ = i;
        x = d;
        y = e;
        z = f;
        startX = this.x;
        startY = this.y;
        startZ = this.z;
        scale = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        float j = this.random.nextFloat() * 0.4F + 0.6F;
        colorRed = j * 0.8F;
        colorGreen = 1;
        colorBlue = j * 0.6F;
        maxAge = (int) (Math.random() * 10.0D) + 40;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double dx, double dy, double dz) {
        setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        repositionFromBoundingBox();
    }

    public float getSize(float tickDelta) {
        float f = ((float) age + tickDelta) / (float) maxAge;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.scale * f;
    }

    public int getColorMultiplier(float tint) {
        int i = super.getColorMultiplier(tint);
        float f = (float) age / (float) maxAge;
        f *= f;
        f *= f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int) (f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        if (age++ >= maxAge) {
            markDead();
        } else {
            float f = (float) age / (float) maxAge;
            float g = f;
            f = -f + f * f * 2.0F;
            f = 1.0F - f;
            this.x = this.startX + this.velocityX * (double) f;
            this.y = this.startY + this.velocityY * (double) f + (double) (1.0F - g);
            this.z = this.startZ + this.velocityZ * (double) f;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ArchitectsParticle architectsParticle = new ArchitectsParticle(clientWorld, d, e, f, g, h, i);
            architectsParticle.setSprite(spriteProvider);
            return architectsParticle;
        }
    }
}