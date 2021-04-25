package net.fenrir.thearchitect.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArchitectsFocus extends Item {
    public ArchitectsFocus(Settings settings) {
        super(settings);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)user;
            if (TAPowers.ARCHITECTS_FOCUS.isActive(playerEntity)) {
                if (playerEntity.experienceLevel >= 5) {
                    int i = this.getMaxUseTime(stack) - remainingUseTicks;
                    if (i >= 22 && !world.isClient) {
                        Vec3d blinkPos = RayHelper.findBlinkPos(user, 1F, 100D);
                        if (user.teleport(blinkPos.x, blinkPos.y, blinkPos.z, false)) {
                            playerEntity.addExperienceLevels(-5);
                            playerEntity.getItemCooldownManager().set(stack.getItem(),100);
                            user.world.playSound(null, user.prevX, user.prevY, user.prevZ, SoundEvents.BLOCK_CONDUIT_DEACTIVATE, user.getSoundCategory(), 1.0F, 1.0F);
                            user.world.playSound(null, blinkPos.x, blinkPos.y, blinkPos.z, SoundEvents.BLOCK_CONDUIT_DEACTIVATE, user.getSoundCategory(), 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (TAPowers.ARCHITECTS_FOCUS.isActive(user)) {
            user.setCurrentHand(hand);
            return TypedActionResult.pass(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.fail(itemStack);
        }
    }
}
