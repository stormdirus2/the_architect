package net.fenrir.thearchitect.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public class Anvil {

    @Shadow
    @Final
    private Property levelCost;

    @Inject(
            method = "canTakeOutput",
            at = @At("HEAD"),
            cancellable = true
    )
    public void canAlwaysUse(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        if (TAPowers.PERITIA_EFFICIENCY.isActive(player)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "onTakeOutput",
            at = @At("HEAD")
    )
    public void revertCost(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (TAPowers.PERITIA_EFFICIENCY.isActive(player)) {
            player.addExperienceLevels(this.levelCost.get());
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "getLevelCost",
            at = @At("HEAD"),
            cancellable = true
    )
    public void noCost(CallbackInfoReturnable<Integer> cir) {
        if (TAPowers.PERITIA_EFFICIENCY.isActive(MinecraftClient.getInstance().player)) {
            cir.setReturnValue(0);
        }
    }
}
