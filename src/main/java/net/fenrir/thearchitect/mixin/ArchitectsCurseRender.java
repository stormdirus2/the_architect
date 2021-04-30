package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.TheArchitect;
import net.fenrir.thearchitect.common.ArchitectsCurseAccessor;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ShulkerBulletEntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerBulletEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBulletEntityRenderer.class)
public abstract class ArchitectsCurseRender extends EntityRenderer<ShulkerBulletEntity> {

    private final static Identifier ARCHITECT_CURSE = new Identifier(TheArchitect.MODID, "textures/entities/architect_curse.png");
    private final static RenderLayer LAYER2 = RenderLayer.getEntityTranslucent(ARCHITECT_CURSE);
    @Final
    @Shadow
    private static RenderLayer LAYER;
    @Final
    @Shadow
    private ShulkerBulletEntityModel<ShulkerBulletEntity> model;

    protected ArchitectsCurseRender(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Shadow
    public abstract Identifier getTexture(ShulkerBulletEntity shulkerBulletEntity);

    /**
     * @author Stormdirus
     */
    @Overwrite
    public void render(ShulkerBulletEntity shulkerBulletEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = MathHelper.lerpAngle(shulkerBulletEntity.prevYaw, shulkerBulletEntity.yaw, g);
        float j = MathHelper.lerp(g, shulkerBulletEntity.prevPitch, shulkerBulletEntity.pitch);
        float k = (float) shulkerBulletEntity.age + g;
        matrixStack.translate(0.0D, 0.15000000596046448D, 0.0D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.sin(k * 0.1F) * 180.0F));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.cos(k * 0.1F) * 180.0F));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(k * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setAngles(shulkerBulletEntity, 0.0F, 0.0F, 0.0F, h, j);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(shulkerBulletEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(this.chooseLayer(shulkerBulletEntity));
        this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStack.pop();
        super.render(shulkerBulletEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Inject(
            method = "getTexture",
            at = @At("HEAD"),
            cancellable = true
    )
    public void replaceTexture(ShulkerBulletEntity shulkerBulletEntity, CallbackInfoReturnable<Identifier> cir) {
        if (((ArchitectsCurseAccessor) shulkerBulletEntity).isCurse()) {
            cir.setReturnValue(ARCHITECT_CURSE);
        }
    }

    public RenderLayer chooseLayer(ShulkerBulletEntity shulkerBulletEntity) {
        if (((ArchitectsCurseAccessor) shulkerBulletEntity).isCurse()) {
            return LAYER2;
        } else {
            return LAYER;
        }
    }

}
