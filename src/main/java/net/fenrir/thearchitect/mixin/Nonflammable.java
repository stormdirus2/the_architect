package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class Nonflammable {

    @Shadow private int fireTicks;

    @Inject(
        method = "setFireTicks",
        at = @At("HEAD"),
        cancellable = true
    )
    public void preventFire(int ticks, CallbackInfo ci) {
        if (TAPowers.NONFLAMMABLE.isActive((Entity) (Object) this)) {
            this.fireTicks = 0;
            ci.cancel();
        }
    }
}
