package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.PowerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = InGameHud.class, priority = 999)
public class AttackHud {


    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isAlive()Z"
            )
    )
    public boolean isAlive(Entity entity) {
        if (PowerHelper.isDimensionallyDisplacing(entity) || PowerHelper.isDimensionallyDisplacing(MinecraftClient.getInstance().player)) {
            return false;
        }
        return entity.isAlive();
    }


}
