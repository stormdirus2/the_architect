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
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
        if (user instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) user;
            if (TAPowers.ARCHITECTS_FOCUS.isActive(playerEntity)) {
                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                if (i >= 22) {
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
                                EntityHitResult result = RayHelper.raycast(user, startPoint, endPoint, new Box(user.getBlockPos()).expand(range), EntityPredicates.VALID_ENTITY, range * range);
                                Entity comeWith = null;
                                if (result != null && result.getEntity() instanceof LivingEntity) {
                                    comeWith = result.getEntity();
                                }
                                if (comeWith != null) {
                                    if (comeWith instanceof ServerPlayerEntity) {
                                        ServerPlayerEntity comeWithPlayer = (ServerPlayerEntity) comeWith;
                                        comeWithPlayer.teleport(realWorld2, pos.getX(), pos.getY(), pos.getZ(), comeWithPlayer.yaw, comeWithPlayer.pitch);
                                        comeWithPlayer.onTeleportationDone();
                                        comeWithPlayer.addExperience(0);
                                    } else {
                                        comeWith.setWorld(realWorld2);
                                        comeWith.teleport(pos.getX(), pos.getY(), pos.getZ());
                                        comeWith.resetPosition(pos.getX(), pos.getY(), pos.getZ());
                                        comeWith.updateNeeded = true;
                                        comeWith.updatePosition(pos.getX(), pos.getY(), pos.getZ());
                                        realWorld2.onDimensionChanged(comeWith);
                                    }
                                }
                                RiftHelper.createRift(world, playerEntity.getBlockPos());
                                playerEntity.teleport(realWorld2, pos.getX(), pos.getY(), pos.getZ(), playerEntity.yaw, playerEntity.pitch);
                                playerEntity.onTeleportationDone();
                                playerEntity.addExperience(0);
                                RiftHelper.createRift(realWorld2, pos);
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
