package net.fenrir.thearchitect.common;

import com.mojang.serialization.DataResult;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ArchitectsFocus extends Item {
    public Logger log = LogManager.getLogger();

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
            PlayerEntity playerEntity = (PlayerEntity) user;
            if (TAPowers.ARCHITECTS_FOCUS.isActive(playerEntity)) {
                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                if (i >= 22 && !world.isClient) {
                    CompoundTag compoundTag = stack.getOrCreateSubTag(TheArchitect.MODID);
                    Optional<RegistryKey<World>> world2 = World.CODEC.parse(NbtOps.INSTANCE, compoundTag.get("warp_world")).result();
                    if (compoundTag.contains("warp_pos") && world2.isPresent()) {
                        BlockPos pos = NbtHelper.toBlockPos(compoundTag.getCompound("warp_pos"));
                        MinecraftServer server = world.getServer();
                        if (server != null) {
                            ServerWorld realWorld2 = server.getWorld(world2.get());
                            if (realWorld2 != null) {
                                int range = 30;
                                Vec3d startPoint = user.getCameraPosVec(1);
                                Vec3d lookVec = user.getRotationVec(1);
                                Vec3d endPoint = startPoint.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
                                EntityHitResult result = RayHelper.raycast(user, startPoint, endPoint, new Box(user.getBlockPos()).expand(range), Entity::isLiving, range * range);
                                Entity comeWith = null;
                                if (result != null) {
                                    comeWith = result.getEntity();
                                }
                                if (realWorld2 != world) {
                                    if (comeWith != null) {
                                        comeWith.moveToWorld(realWorld2);
                                    }
                                    playerEntity.moveToWorld(realWorld2);
                                }
                                if (comeWith != null) {
                                    comeWith.teleport(pos.getX(), pos.getY(), pos.getZ());
                                }
                                playerEntity.teleport(pos.getX(), pos.getY(), pos.getZ());
                                world.playSound(null, user.prevX, user.prevY, user.prevZ, SoundEvents.BLOCK_CONDUIT_DEACTIVATE, user.getSoundCategory(), 1.0F, 1.0F);
                                realWorld2.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CONDUIT_DEACTIVATE, user.getSoundCategory(), 1.0F, 1.0F);
                                playerEntity.getItemCooldownManager().set(stack.getItem(), 200);
                            }
                        }
                    } else {
                        compoundTag.put("warp_pos", NbtHelper.fromBlockPos(playerEntity.getBlockPos()));
                        DataResult<Tag> dataResult = World.CODEC.encodeStart(NbtOps.INSTANCE, world.getRegistryKey());
                        dataResult.resultOrPartial(log::error).ifPresent((tag) -> {
                            compoundTag.put("warp_world", tag);
                        });
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
